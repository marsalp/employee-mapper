package com.mapper.employee_mapper.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines how SDK Employee fields map to our domain Employee fields.
 */
public class EmployeeMappingRegistry implements BaseMappingRegistry {
    private static final Map<String, String> FIELD_MAPPINGS = new HashMap<>();

    static {
        FIELD_MAPPINGS.put("email", "workEmail");
        FIELD_MAPPINGS.put("company", "company");
    }

    @Override
    public Map<String, String> getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
