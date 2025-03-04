package com.mapper.employee_mapper.merge_sdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Simulates the Merge "Company" object with nested or complex fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SdkCompany {
    private String id;
    private String legalName;
    
    // Let's simulate a nested object:
    private SdkAddress headquartersAddress;
    
    // Possibly more fields/lists if you like to mimic complexity...
}
