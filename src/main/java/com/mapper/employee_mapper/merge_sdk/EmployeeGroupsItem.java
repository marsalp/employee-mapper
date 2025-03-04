package com.mapper.employee_mapper.merge_sdk;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Objects;

@JsonDeserialize(
        using = EmployeeGroupsItem.Deserializer.class
)
public final class EmployeeGroupsItem {
    private final Object value;
    private final int type;

    private EmployeeGroupsItem(Object value, int type) {
        this.value = value;
        this.type = type;
    }

    @JsonValue
    public Object get() {
        return this.value;
    }

    public <T> T visit(Visitor<T> visitor) {
        if (this.type == 0) {
            return (T)visitor.visit((String)this.value);
        } else if (this.type == 1) {
            return (T)visitor.visit((Group)this.value);
        } else {
            throw new IllegalStateException("Failed to visit value. This should never happen.");
        }
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof EmployeeGroupsItem && this.equalTo((EmployeeGroupsItem)other);
        }
    }

    private boolean equalTo(EmployeeGroupsItem other) {
        return this.value.equals(other.value);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.value});
    }

    public String toString() {
        return this.value.toString();
    }

    public static EmployeeGroupsItem of(String value) {
        return new EmployeeGroupsItem(value, 0);
    }

    public static EmployeeGroupsItem of(Group value) {
        return new EmployeeGroupsItem(value, 1);
    }

    static final class Deserializer extends StdDeserializer<EmployeeGroupsItem> {
        Deserializer() {
            super(EmployeeGroupsItem.class);
        }

        public EmployeeGroupsItem deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Object value = p.readValueAs(Object.class);

            try {
                return EmployeeGroupsItem.of((String)ObjectMappers.JSON_MAPPER.convertValue(value, String.class));
            } catch (IllegalArgumentException var6) {
                try {
                    return EmployeeGroupsItem.of((Group)ObjectMappers.JSON_MAPPER.convertValue(value, Group.class));
                } catch (IllegalArgumentException var5) {
                    throw new JsonParseException(p, "Failed to deserialize");
                }
            }
        }
    }

    public interface Visitor<T> {
        T visit(String var1);

        T visit(Group var1);
    }
}

