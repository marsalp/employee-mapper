package com.mapper.employee_mapper.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Our simple internal Employee model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee implements HasExtraFields {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Company company;
    private Map<String, Object> extraFields;

    @Override
    public void setExtraFields(Map<String, Object> extraFields) {
        this.extraFields = extraFields;
    }
}
