package com.mapper.employee_mapper.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines how SDK Company fields map to our domain Company fields.
 */
public class CompanyMappingRegistry implements BaseMappingRegistry {
    private static final Map<String, String> FIELD_MAPPINGS = new HashMap<>();

    static {
        FIELD_MAPPINGS.put("name", "legalName");
        FIELD_MAPPINGS.put("address", "headquartersAddress");
    }

    @Override
    public Map<String, String> getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
