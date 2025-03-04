package com.mapper.employee_mapper.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Our simple internal Company model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    private String id;
    private String name;
    private Address address;
    // e.g. "legalName" from Merge

    // We keep it minimal and avoid replicating everything from the Merge object.
}
