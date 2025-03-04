package com.mapper.employee_mapper.merge_sdk;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@JsonInclude(Include.NON_EMPTY)
@JsonDeserialize(
        builder = Builder.class
)
public final class RemoteData {
    private final String path;
    private final Optional<JsonNode> data;
    private final Map<String, Object> additionalProperties;

    private RemoteData(String path, Optional<JsonNode> data, Map<String, Object> additionalProperties) {
        this.path = path;
        this.data = data;
        this.additionalProperties = additionalProperties;
    }

    @JsonProperty("path")
    public String getPath() {
        return this.path;
    }

    @JsonProperty("data")
    public Optional<JsonNode> getData() {
        return this.data;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof RemoteData && this.equalTo((RemoteData)other);
        }
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    private boolean equalTo(RemoteData other) {
        return this.path.equals(other.path) && this.data.equals(other.data);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.path, this.data});
    }

    public String toString() {
        return ObjectMappers.stringify(this);
    }

    public static PathStage builder() {
        return new Builder();
    }

    @JsonIgnoreProperties(
            ignoreUnknown = true
    )
    public static final class Builder implements PathStage, _FinalStage {
        private String path;
        private Optional<JsonNode> data;
        @JsonAnySetter
        private Map<String, Object> additionalProperties;

        private Builder() {
            this.data = Optional.empty();
            this.additionalProperties = new HashMap();
        }

        public Builder from(RemoteData other) {
            this.path(other.getPath());
            this.data(other.getData());
            return this;
        }

        @JsonSetter("path")
        public _FinalStage path(String path) {
            this.path = path;
            return this;
        }

        public _FinalStage data(JsonNode data) {
            this.data = Optional.of(data);
            return this;
        }

        @JsonSetter(
                value = "data",
                nulls = Nulls.SKIP
        )
        public _FinalStage data(Optional<JsonNode> data) {
            this.data = data;
            return this;
        }

        public RemoteData build() {
            return new RemoteData(this.path, this.data, this.additionalProperties);
        }
    }

    public interface PathStage {
        _FinalStage path(String var1);

        Builder from(RemoteData var1);
    }

    public interface _FinalStage {
        RemoteData build();

        _FinalStage data(Optional<JsonNode> var1);

        _FinalStage data(JsonNode var1);
    }
}
