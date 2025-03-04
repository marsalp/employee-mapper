package com.mapper.employee_mapper.merge_sdk;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

public final class ObjectMappers {
    public static final ObjectMapper JSON_MAPPER;

    private ObjectMappers() {
    }

    public static String stringify(Object o) {
        try {
            return JSON_MAPPER.setSerializationInclusion(Include.ALWAYS).writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (IOException var2) {
            return o.getClass().getName() + "@" + Integer.toHexString(o.hashCode());
        }
    }

    static {
        JSON_MAPPER = ((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)JsonMapper.builder().addModule(new Jdk8Module())).addModule(new JavaTimeModule())).addModule(DateTimeDeserializer.getModule())).disable(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES})).disable(new SerializationFeature[]{SerializationFeature.WRITE_DATES_AS_TIMESTAMPS})).build();
    }
}
