package com.mapper.employee_mapper.controller;

import com.mapper.employee_mapper.domain.Employee;
import com.mapper.employee_mapper.mapper.CompanyMappingRegistry;
import com.mapper.employee_mapper.mapper.EmployeeMappingRegistry;
import com.mapper.employee_mapper.mapper.UniversalReflectionMapper;
import com.mapper.employee_mapper.merge_sdk.EmployeeGroupsItem;
import com.mapper.employee_mapper.merge_sdk.Group;
import com.mapper.employee_mapper.merge_sdk.SdkAddress;
import com.mapper.employee_mapper.merge_sdk.SdkCompany;
import com.mapper.employee_mapper.merge_sdk.SdkEmployee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@RestController
public class PocSingleMapperController {

    private final UniversalReflectionMapper universalMapper;

    public PocSingleMapperController() {
        // 1) Creamos el mapper y registramos las clases de dominio
        this.universalMapper = new UniversalReflectionMapper()
                .registerMapping(com.mapper.employee_mapper.domain.Employee.class, new EmployeeMappingRegistry())
                .registerMapping(com.mapper.employee_mapper.domain.Company.class, new CompanyMappingRegistry());
    }

    /**
     * Endpoint único: GET /poc/transform
     * Retorna un Employee (dominio) mapeado desde un SdkEmployee con varios campos.
     */
    @GetMapping("/poc/transform")
    public Employee transformWithSingleMapper() {
        // 2) Construimos un SdkEmployee con datos más completos
        SdkEmployee sdkEmployee = buildFullSdkEmployee();

        // 3) Lo mapeamos a nuestro dominio Employee
        Employee domainEmployee = universalMapper.map(sdkEmployee, Employee.class);

        // 4) Lo devolvemos (Spring lo serializa a JSON)
        return domainEmployee;
    }

    /**
     * Crea un SdkEmployee "completo" con direcciones, grupos, empresa anidada, etc.
     * para ilustrar cómo se mapea o va a extraFields.
     */
    private SdkEmployee buildFullSdkEmployee() {
        // Direcciones en addresses
        SdkAddress addr1 = SdkAddress.builder()
                .street("111 Merge Lane")
                .city("MergeCity")
                .country("MergeLand")
                .build();

        SdkAddress addr2 = SdkAddress.builder()
                .street("222 Another St")
                .city("SecondCity")
                .country("AnotherLand")
                .build();

        // Empresa con un headquartersAddress anidado
        SdkCompany sdkCompany = SdkCompany.builder()
                .id("COMP-ABC")
                .legalName("Acme Corporation Intl")
                .headquartersAddress(
                        SdkAddress.builder()
                                .street("HQ 1000 Some Blvd")
                                .city("HeadquarterCity")
                                .country("HeadquarterLand")
                                .build()
                )
                .build();

        // Lista de groups
        Group realGroup = Group.builder()
                .id(Optional.of("GRP-123"))
                .name(Optional.of("Engineering Team"))
                .build();
        // Un item es objeto Group, el otro es String
        EmployeeGroupsItem groupObject = EmployeeGroupsItem.of(realGroup);
        EmployeeGroupsItem groupString = EmployeeGroupsItem.of("Ad-hoc Group");

        List<Optional<EmployeeGroupsItem>> groupsList = Arrays.asList(
                Optional.of(groupObject),
                Optional.of(groupString)
        );

        // Construimos el SdkEmployee final
        return SdkEmployee.builder()
                .id("EMP-1")
                .firstName("Jane")
                .lastName("Doe")
                .workEmail("jane.doe@acme.com")
                .company(Optional.of(sdkCompany))     // Envuelto en Optional
                .addresses(Arrays.asList(addr1, addr2))
                .groups(Optional.of(groupsList))
                .build();
    }
}
