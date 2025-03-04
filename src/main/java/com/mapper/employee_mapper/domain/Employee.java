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
public class Employee {
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    private Company company; // Link to our minimal Company domain

    // A "catch-all" map for extra fields we don't want to hardcode.
    private Map<String, Object> extraFields;
}
