package com.mapper.employee_mapper.merge_sdk;


import com.fasterxml.jackson.annotation.JsonValue;

public enum GroupTypeEnum {
    TEAM("TEAM"),
    DEPARTMENT("DEPARTMENT"),
    COST_CENTER("COST_CENTER"),
    BUSINESS_UNIT("BUSINESS_UNIT"),
    GROUP("GROUP");

    private final String value;

    private GroupTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String toString() {
        return this.value;
    }
}
