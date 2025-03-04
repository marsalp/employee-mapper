package com.mapper.employee_mapper.merge_sdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simulates a nested object within the Merge Employee or Company.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SdkAddress {
    private String street;
    private String city;
    private String country;
}
