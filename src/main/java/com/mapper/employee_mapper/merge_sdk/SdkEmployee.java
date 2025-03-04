package com.mapper.employee_mapper.merge_sdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Simulates the Merge "Employee" object with nested structures.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SdkEmployee {
    private String id;
    private String firstName;
    private String lastName;
    private String workEmail;

    // Here we reference the SdkCompany (nested object).
    private SdkCompany company;

    // Suppose the Employee has a list of addresses (multi-level structure).
    private List<SdkAddress> addresses;
    
    // You can add other nested fields if needed for demonstration.
}
