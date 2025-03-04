package com.mapper.employee_mapper.mapper;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * A universal, reflection-based mapper that transforms a "Merge-like" source object
 * into one of our domain objects (Employee, Company, etc.).
 *
 * - We do NOT create separate classes for each mapping.
 * - We rely on the domain classes having an 'extraFields' Map<String, Object>
 *   to store anything that doesn't map directly.
 * - We do minimal checks for nested domain classes (e.g., if the domain field is "Company",
 *   we attempt to recursively map the source subobject into a new "Company" instance).
 */
@Slf4j
public class UniversalReflectionMapper {
    
    /**
     * The main entry point. Provide:
     *  - `sourceObject` from the "Merge-like" domain (e.g., SdkEmployee)
     *  - `targetClass` from our domain (e.g., Employee.class)
     *
     *  Returns an instance of `targetClass` with mapped fields.
     */
    public <T> T map(Object sourceObject, Class<T> targetClass) {
        if (sourceObject == null) {
            return null;
        }
        return mapObjectToDomain(sourceObject, targetClass);
    }

    /**
     * Creates an instance of targetClass, then tries to fill
     * 1) matching fields, 2) handle nested objects recursively,
     * 3) put anything leftover into 'extraFields' if that exists on the target.
     */
    private <T> T mapObjectToDomain(Object source, Class<T> targetClass) {
        try {
            // Instantiate the domain class
            T targetInstance = targetClass.getDeclaredConstructor().newInstance();

            // We'll keep track of which source field names we successfully mapped
            Set<String> mappedFields = new HashSet<>();

            // Reflect over the target domain class's fields
            Field[] targetFields = targetClass.getDeclaredFields();
            for (Field targetField : targetFields) {
                targetField.setAccessible(true);
                String targetFieldName = targetField.getName();

                // See if the source object has a field with the same name
                Field sourceField = findFieldByName(source.getClass(), targetFieldName);
                if (sourceField != null) {
                    sourceField.setAccessible(true);
                    Object sourceValue = sourceField.get(source);
                    mappedFields.add(targetFieldName);

                    // We'll handle:
                    // - direct simple type copy
                    // - nested domain object
                    // - collection
                    // - fallback -> store in extra if mismatch
                    Object mappedValue = mapValue(sourceValue, targetField.getType());
                    targetField.set(targetInstance, mappedValue);
                }
            }

            // Now handle leftover source fields (the ones we never matched in the domain).
            // If the domain has an 'extraFields' field, we store them there.
            Field extraFieldsField = findFieldByName(targetClass, "extraFields");
            if (extraFieldsField != null) {
                extraFieldsField.setAccessible(true);

                // We'll gather leftover fields into a map
                Map<String, Object> extrasMap = new HashMap<>();
                Field[] sourceFields = source.getClass().getDeclaredFields();
                for (Field sField : sourceFields) {
                    sField.setAccessible(true);
                    String sFieldName = sField.getName();
                    if (mappedFields.contains(sFieldName)) {
                        continue; // Already mapped
                    }
                    Object leftoverValue = sField.get(source);
                    if (leftoverValue != null) {
                        // We'll either store it "as is" or transform it into a Map if it's complex
                        Object leftoverMapped = mapComplexToMap(leftoverValue);
                        extrasMap.put(sFieldName, leftoverMapped);
                    }
                }
                extraFieldsField.set(targetInstance, extrasMap);
            }

            return targetInstance;
        } catch (Exception e) {
            log.error("Error mapping object of type {} to domain class {}",
                    source.getClass().getName(), targetClass.getName(), e);
            return null;
        }
    }

    /**
     * Attempts to map the 'sourceValue' to the 'targetType' (which is the type
     * of a field in the domain class).
     */
    private Object mapValue(Object sourceValue, Class<?> targetType) {
        if (sourceValue == null) {
            return null;
        }
        Class<?> sourceType = sourceValue.getClass();

        // If it's a collection, map each element individually
        if (Collection.class.isAssignableFrom(sourceType)) {
            Collection<?> srcCollection = (Collection<?>) sourceValue;
            // We'll return a List if the target is List, or keep it as something else otherwise
            List<Object> mappedList = new ArrayList<>();
            for (Object item : srcCollection) {
                // We check if targetType is something like List<Company> or List<String>?
                // Reflection doesn't know the generic type. We'll just assume the domain field
                // is a List of domain objects or we store them as maps if domain doesn't define it.
                mappedList.add(mapComplexToMap(item));
            }
            // If the domain field is a List, return the mappedList directly
            if (List.class.isAssignableFrom(targetType)) {
                return mappedList;
            }
            // Otherwise, you might do more logic, but let's keep it simple
            return mappedList;
        }

        // If it's a "simple" type: (String, Number, Boolean, etc.), copy as is
        if (isSimpleType(sourceType)) {
            // If the domain field is also a simple type or a String, just cast
            if (isSimpleType(targetType)) {
                return sourceValue;
            }
        }

        // If the domain field is one of our domain classes, recursively map
        if (isDomainClass(targetType)) {
            return mapObjectToDomain(sourceValue, targetType);
        }

        // If none of the above, store as a Map (so we don't lose data)
        return mapComplexToMap(sourceValue);
    }

    /**
     * Turn an object into a map by reflecting its fields (recursively).
     * We'll do a simpler approach here: reflect all fields into a map.
     */
    private Map<String, Object> mapComplexToMap(Object complexValue) {
        if (complexValue == null) return Collections.emptyMap();

        if (isSimpleType(complexValue.getClass())) {
            // A "simple" type is returned as-is in a map with a 'value' key, for instance
            return Map.of("value", complexValue);
        }
        if (complexValue instanceof Collection) {
            // Convert each item in the collection
            Collection<?> coll = (Collection<?>) complexValue;
            List<Object> mappedList = new ArrayList<>();
            for (Object item : coll) {
                mappedList.add(mapComplexToMap(item));
            }
            return Map.of("list", mappedList);
        }

        Map<String, Object> result = new HashMap<>();
        Field[] fields = complexValue.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Object val = f.get(complexValue);
                if (val != null) {
                    if (isSimpleType(val.getClass())) {
                        result.put(f.getName(), val);
                    } else if (val instanceof Collection) {
                        // Convert each item in the collection
                        Collection<?> c = (Collection<?>) val;
                        List<Object> subList = new ArrayList<>();
                        for (Object item : c) {
                            subList.add(mapComplexToMap(item));
                        }
                        result.put(f.getName(), subList);
                    } else if (isDomainClass(val.getClass())) {
                        // If it's a domain class, we might want to map it too
                        // But let's assume if we're converting to a map, we keep it as a map
                        result.put(f.getName(), mapComplexToMap(val));
                    } else {
                        // Another complex object
                        result.put(f.getName(), mapComplexToMap(val));
                    }
                }
            } catch (IllegalAccessException e) {
                log.warn("Error reading field {} from {}", f.getName(), complexValue.getClass(), e);
            }
        }
        return result;
    }

    /**
     * Utility: find a field by name in a given class (or its superclasses).
     */
    private Field findFieldByName(Class<?> clazz, String name) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    /**
     * Check if the class is "simple" (String, Number, Boolean, Character, etc.).
     */
    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive()
                || Number.class.isAssignableFrom(clazz)
                || CharSequence.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)
                || UUID.class.isAssignableFrom(clazz);
    }

    /**
     * Check if the class is one of our domain classes (Employee, Company, etc.).
     * For a simple approach, let's just see if it's in a certain package
     * or if we keep a set of domain classes.
     */
    private boolean isDomainClass(Class<?> clazz) {
        // Example: if your domain is in package "com.mapper.employee_mapper.domain":
        return clazz.getPackageName().contains("com.mapper.employee_mapper.domain");
    }
}
