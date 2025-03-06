package com.mapper.employee_mapper.mapper;

import com.mapper.employee_mapper.domain.HasExtraFields;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * A universal reflection-based mapper that transforms a "SDK-like" source object
 * into one of our domain objects (Employee, Company, etc.).
 *
 * This version does not handle final fields or custom getter-based reflection
 * logic. It simply looks for matching fields by name, unwrapping Optionals,
 * and storing everything unmapped into extraFields.
 */
@Slf4j
public class UniversalReflectionMapper {

    private final Map<Class<?>, BaseMappingRegistry> registryMap = new HashMap<>();

    /**
     * Fluent method to register a domain class + its mapping registry.
     * Example usage:
     *   universalMapper
     *      .registerMapping(Employee.class, new EmployeeMappingRegistry())
     *      .registerMapping(Company.class, new CompanyMappingRegistry());
     */
    public UniversalReflectionMapper registerMapping(Class<?> domainClass, BaseMappingRegistry registry) {
        this.registryMap.put(domainClass, registry);
        return this;
    }

    /**
     * Main entry point: maps the given sourceObject (SDK object) to an instance
     * of the specified domain type.
     */
    public <T> T map(Object sourceObject, Class<T> targetClass) {
        if (sourceObject == null) {
            log.warn("Source object is null; returning null for {}", targetClass.getSimpleName());
            return null;
        }
        return mapObjectToDomain(sourceObject, targetClass);
    }

    /**
     * Recursively maps sourceObject → target domain object using reflection.
     */
    private <T> T mapObjectToDomain(Object source, Class<T> targetClass) {
        try {
            // Unwrap Optional if present
            if (source instanceof Optional) {
                source = ((Optional<?>) source).orElse(null);
                if (source == null) {
                    return null;
                }
            }

            T targetInstance = targetClass.getDeclaredConstructor().newInstance();
            BaseMappingRegistry registry = registryMap.get(targetClass);

            Set<String> mappedDomainFields = new HashSet<>();

            // Reflect over the domain's fields
            Field[] domainFields = targetClass.getDeclaredFields();
            for (Field domainField : domainFields) {
                domainField.setAccessible(true);
                String domainFieldName = domainField.getName();

                // Determine the SDK field name via registry if present
                String sdkFieldName = (registry != null)
                        ? registry.getFieldMappings().getOrDefault(domainFieldName, domainFieldName)
                        : domainFieldName;

                // Attempt to find a field with that name on the source's class
                Field sdkField = findFieldByName(source.getClass(), sdkFieldName);
                if (sdkField != null) {
                    sdkField.setAccessible(true);
                    Object sdkValue = sdkField.get(source);

                    // Unwrap if it's an Optional
                    if (sdkValue instanceof Optional) {
                        sdkValue = ((Optional<?>) sdkValue).orElse(null);
                    }

                    // If the target field type also has a registry, treat it as nested domain
                    Object mappedValue;
                    if (registryMap.containsKey(domainField.getType()) && sdkValue != null) {
                        mappedValue = mapObjectToDomain(sdkValue, domainField.getType());
                    } else {
                        mappedValue = mapValue(sdkValue, domainField.getType());
                    }

                    domainField.set(targetInstance, mappedValue);
                    mappedDomainFields.add(domainFieldName);
                }
            }

            // For leftover SDK fields, store them in extraFields if domain implements HasExtraFields
            if (targetInstance instanceof HasExtraFields) {
                Map<String, Object> leftover = buildLeftoverStructure(source, mappedDomainFields, targetClass);
                if (!leftover.isEmpty()) {
                    ((HasExtraFields)targetInstance).setExtraFields(leftover);
                }
            }

            return targetInstance;
        } catch (Exception e) {
            log.error("Error mapping from {} to {}",
                    source.getClass().getSimpleName(), targetClass.getSimpleName(), e);
            return null;
        }
    }

    /**
     * Builds a structure (Map) of leftover fields from the SDK object that were not mapped
     * to any domain field, storing them in extraFields if applicable.
     */
    private Map<String, Object> buildLeftoverStructure(
            Object source, Set<String> mappedDomainFields, Class<?> domainClass) {

        Map<String, Object> leftover = new LinkedHashMap<>();
        Field[] sdkFields = source.getClass().getDeclaredFields();
        for (Field sdkField : sdkFields) {
            sdkField.setAccessible(true);
            String sdkFieldName = sdkField.getName();

            if (!didWeMapThisSdkField(sdkFieldName, mappedDomainFields, domainClass)) {
                try {
                    Object sdkVal = sdkField.get(source);
                    leftover.put(sdkFieldName, buildLeftoverValue(sdkVal));
                } catch (IllegalAccessException e) {
                    log.warn("Unable to read leftover field {}: {}", sdkFieldName, e.getMessage());
                }
            }
        }
        return leftover;
    }

    /**
     * Checks if a given SDK field name was mapped to any domain field in this class's registry.
     */
    private boolean didWeMapThisSdkField(String sdkFieldName, Set<String> mappedDomainFields, Class<?> domainClass) {
        BaseMappingRegistry registry = registryMap.get(domainClass);
        if (registry == null) {
            // If no registry, we only map domain fields if domainField == sdkFieldName
            return mappedDomainFields.contains(sdkFieldName);
        }

        // If we do have a registry, check each domain field we mapped
        for (String domainFieldName : mappedDomainFields) {
            String mappedSdkName = registry.getFieldMappings().get(domainFieldName);
            if (mappedSdkName == null) {
                // Means domainFieldName -> same name as sdk?
                if (domainFieldName.equals(sdkFieldName)) {
                    return true;
                }
            } else {
                if (mappedSdkName.equals(sdkFieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Recursively constructs leftover data for unmapped fields.
     * - If it's a collection, transform each item.
     * - If it's a simple type, use it directly.
     * - Otherwise, reflect its fields into a map.
     */
    private Object buildLeftoverValue(Object sdkVal) {
        if (sdkVal == null) return null;

        // Unwrap Optional
        if (sdkVal instanceof Optional) {
            sdkVal = ((Optional<?>) sdkVal).orElse(null);
            if (sdkVal == null) return null;
        }

        // If it's a collection, map each element
        if (sdkVal instanceof Collection) {
            Collection<?> coll = (Collection<?>) sdkVal;
            List<Object> resultList = new ArrayList<>();
            for (Object item : coll) {
                resultList.add(buildLeftoverValue(item));
            }
            return resultList;
        }

        // If it's a "simple" type, return as is
        if (isSimpleType(sdkVal.getClass())) {
            return sdkVal;
        }

        // Otherwise, treat it as a complex object => reflect and build a map
        Map<String, Object> objMap = new LinkedHashMap<>();
        Field[] fields = sdkVal.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Object val = f.get(sdkVal);
                objMap.put(f.getName(), buildLeftoverValue(val));
            } catch (Exception e) {
                objMap.put(f.getName(), "ERROR_READING_FIELD");
            }
        }
        return objMap;
    }

    private Field findFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * If the domain field is e.g. a String, and the SDK value is also a String,
     * we can directly assign. Otherwise, return null (fallback).
     */
    private Object mapValue(Object sourceValue, Class<?> targetType) {
        if (sourceValue == null) return null;
        if (targetType.isAssignableFrom(sourceValue.getClass())) {
            return sourceValue;
        }
        return null;
    }

    /**
     * A helper check to see if a class is a "simple" type (String, Number, Boolean, etc.)
     */
    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive()
                || Number.class.isAssignableFrom(clazz)
                || CharSequence.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)
                || UUID.class.isAssignableFrom(clazz)
                || clazz.isEnum();
    }
}
