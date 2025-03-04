package com.mapper.employee_mapper.merge_sdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Simulates the Merge "Employee" object with nested structures.
 */
@Data
@AllArgsConstructor
@Builder
public class SdkEmployee {
    private String id;
    private String firstName;
    private String lastName;
    private String workEmail;

    // Here we reference the SdkCompany (nested object).
    private final Optional<SdkCompany> company;

    // Suppose the Employee has a list of addresses (multi-level structure).
    private List<SdkAddress> addresses;

    private final Optional<List<Optional<EmployeeGroupsItem>>> groups;
    // You can add other nested fields if needed for demonstration.
}
