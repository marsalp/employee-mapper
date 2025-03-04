package com.mapper.employee_mapper.controller;

import com.mapper.employee_mapper.domain.Employee;
import com.mapper.employee_mapper.mapper.UniversalReflectionMapper;
import com.mapper.employee_mapper.merge_sdk.SdkAddress;
import com.mapper.employee_mapper.merge_sdk.SdkCompany;
import com.mapper.employee_mapper.merge_sdk.SdkEmployee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PocSingleMapperController {

    private final UniversalReflectionMapper universalMapper = new UniversalReflectionMapper();

    @GetMapping("/poc/transform")
    public Employee transformWithSingleMapper() {
        // 1) Build a sample SdkEmployee (simulating a Merge response)
        SdkEmployee sdkEmployee = createFakeSdkEmployee();

        // 2) Use the universal reflection-based mapper 
        //    to map it into our domain Employee class
        Employee domainEmployee = universalMapper.map(sdkEmployee, Employee.class);

        // 3) Return the domainEmployee (Spring Boot returns JSON)
        return domainEmployee;
    }

    private SdkEmployee createFakeSdkEmployee() {
        SdkAddress address1 = SdkAddress.builder()
                .street("123 Merge Lane")
                .city("MergeCity")
                .country("MergeLand")
                .build();

        SdkAddress address2 = SdkAddress.builder()
                .street("456 Another St")
                .city("SecondCity")
                .country("AnotherLand")
                .build();

        SdkCompany company = SdkCompany.builder()
                .id("COMP-1")
                .legalName("Acme Corporation")
                .headquartersAddress(address1)
                .build();

        return SdkEmployee.builder()
                .id("EMP-1")
                .firstName("Jane")
                .lastName("Doe")
                .workEmail("jane.doe@acme.com")
                .company(Optional.ofNullable(company))
                .addresses(List.of(address1, address2))
                .build();
    }

}
