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

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@JsonInclude(Include.NON_EMPTY)
@JsonDeserialize(
        builder = Builder.class
)
public final class Group {
    private final Optional<String> id;
    private final Optional<String> remoteId;
    private final Optional<OffsetDateTime> createdAt;
    private final Optional<OffsetDateTime> modifiedAt;
    private final Optional<String> parentGroup;
    private final Optional<String> name;
    private final Optional<GroupType> type;
    private final Optional<Boolean> remoteWasDeleted;
    private final Optional<Boolean> isCommonlyUsedAsTeam;
    private final Optional<Map<String, JsonNode>> fieldMappings;
    private final Optional<List<RemoteData>> remoteData;
    private final Map<String, Object> additionalProperties;

    private Group(Optional<String> id, Optional<String> remoteId, Optional<OffsetDateTime> createdAt, Optional<OffsetDateTime> modifiedAt, Optional<String> parentGroup, Optional<String> name, Optional<GroupType> type, Optional<Boolean> remoteWasDeleted, Optional<Boolean> isCommonlyUsedAsTeam, Optional<Map<String, JsonNode>> fieldMappings, Optional<List<RemoteData>> remoteData, Map<String, Object> additionalProperties) {
        this.id = id;
        this.remoteId = remoteId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.parentGroup = parentGroup;
        this.name = name;
        this.type = type;
        this.remoteWasDeleted = remoteWasDeleted;
        this.isCommonlyUsedAsTeam = isCommonlyUsedAsTeam;
        this.fieldMappings = fieldMappings;
        this.remoteData = remoteData;
        this.additionalProperties = additionalProperties;
    }

    @JsonProperty("id")
    public Optional<String> getId() {
        return this.id;
    }

    @JsonProperty("remote_id")
    public Optional<String> getRemoteId() {
        return this.remoteId;
    }

    @JsonProperty("created_at")
    public Optional<OffsetDateTime> getCreatedAt() {
        return this.createdAt;
    }

    @JsonProperty("modified_at")
    public Optional<OffsetDateTime> getModifiedAt() {
        return this.modifiedAt;
    }

    @JsonProperty("parent_group")
    public Optional<String> getParentGroup() {
        return this.parentGroup;
    }

    @JsonProperty("name")
    public Optional<String> getName() {
        return this.name;
    }

    @JsonProperty("type")
    public Optional<GroupType> getType() {
        return this.type;
    }

    @JsonProperty("remote_was_deleted")
    public Optional<Boolean> getRemoteWasDeleted() {
        return this.remoteWasDeleted;
    }

    @JsonProperty("is_commonly_used_as_team")
    public Optional<Boolean> getIsCommonlyUsedAsTeam() {
        return this.isCommonlyUsedAsTeam;
    }

    @JsonProperty("field_mappings")
    public Optional<Map<String, JsonNode>> getFieldMappings() {
        return this.fieldMappings;
    }

    @JsonProperty("remote_data")
    public Optional<List<RemoteData>> getRemoteData() {
        return this.remoteData;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof Group && this.equalTo((Group)other);
        }
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    private boolean equalTo(Group other) {
        return this.id.equals(other.id) && this.remoteId.equals(other.remoteId) && this.createdAt.equals(other.createdAt) && this.modifiedAt.equals(other.modifiedAt) && this.parentGroup.equals(other.parentGroup) && this.name.equals(other.name) && this.type.equals(other.type) && this.remoteWasDeleted.equals(other.remoteWasDeleted) && this.isCommonlyUsedAsTeam.equals(other.isCommonlyUsedAsTeam) && this.fieldMappings.equals(other.fieldMappings) && this.remoteData.equals(other.remoteData);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id, this.remoteId, this.createdAt, this.modifiedAt, this.parentGroup, this.name, this.type, this.remoteWasDeleted, this.isCommonlyUsedAsTeam, this.fieldMappings, this.remoteData});
    }

    public String toString() {
        return ObjectMappers.stringify(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonIgnoreProperties(
            ignoreUnknown = true
    )
    public static final class Builder {
        private Optional<String> id;
        private Optional<String> remoteId;
        private Optional<OffsetDateTime> createdAt;
        private Optional<OffsetDateTime> modifiedAt;
        private Optional<String> parentGroup;
        private Optional<String> name;
        private Optional<GroupType> type;
        private Optional<Boolean> remoteWasDeleted;
        private Optional<Boolean> isCommonlyUsedAsTeam;
        private Optional<Map<String, JsonNode>> fieldMappings;
        private Optional<List<RemoteData>> remoteData;
        @JsonAnySetter
        private Map<String, Object> additionalProperties;

        private Builder() {
            this.id = Optional.empty();
            this.remoteId = Optional.empty();
            this.createdAt = Optional.empty();
            this.modifiedAt = Optional.empty();
            this.parentGroup = Optional.empty();
            this.name = Optional.empty();
            this.type = Optional.empty();
            this.remoteWasDeleted = Optional.empty();
            this.isCommonlyUsedAsTeam = Optional.empty();
            this.fieldMappings = Optional.empty();
            this.remoteData = Optional.empty();
            this.additionalProperties = new HashMap();
        }

        public Builder from(Group other) {
            this.id(other.getId());
            this.remoteId(other.getRemoteId());
            this.createdAt(other.getCreatedAt());
            this.modifiedAt(other.getModifiedAt());
            this.parentGroup(other.getParentGroup());
            this.name(other.getName());
            this.type(other.getType());
            this.remoteWasDeleted(other.getRemoteWasDeleted());
            this.isCommonlyUsedAsTeam(other.getIsCommonlyUsedAsTeam());
            this.fieldMappings(other.getFieldMappings());
            this.remoteData(other.getRemoteData());
            return this;
        }

        @JsonSetter(
                value = "id",
                nulls = Nulls.SKIP
        )
        public Builder id(Optional<String> id) {
            this.id = id;
            return this;
        }

        public Builder id(String id) {
            this.id = Optional.of(id);
            return this;
        }

        @JsonSetter(
                value = "remote_id",
                nulls = Nulls.SKIP
        )
        public Builder remoteId(Optional<String> remoteId) {
            this.remoteId = remoteId;
            return this;
        }

        public Builder remoteId(String remoteId) {
            this.remoteId = Optional.of(remoteId);
            return this;
        }

        @JsonSetter(
                value = "created_at",
                nulls = Nulls.SKIP
        )
        public Builder createdAt(Optional<OffsetDateTime> createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = Optional.of(createdAt);
            return this;
        }

        @JsonSetter(
                value = "modified_at",
                nulls = Nulls.SKIP
        )
        public Builder modifiedAt(Optional<OffsetDateTime> modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        public Builder modifiedAt(OffsetDateTime modifiedAt) {
            this.modifiedAt = Optional.of(modifiedAt);
            return this;
        }

        @JsonSetter(
                value = "parent_group",
                nulls = Nulls.SKIP
        )
        public Builder parentGroup(Optional<String> parentGroup) {
            this.parentGroup = parentGroup;
            return this;
        }

        public Builder parentGroup(String parentGroup) {
            this.parentGroup = Optional.of(parentGroup);
            return this;
        }

        @JsonSetter(
                value = "name",
                nulls = Nulls.SKIP
        )
        public Builder name(Optional<String> name) {
            this.name = name;
            return this;
        }

        public Builder name(String name) {
            this.name = Optional.of(name);
            return this;
        }

        @JsonSetter(
                value = "type",
                nulls = Nulls.SKIP
        )
        public Builder type(Optional<GroupType> type) {
            this.type = type;
            return this;
        }

        public Builder type(GroupType type) {
            this.type = Optional.of(type);
            return this;
        }

        @JsonSetter(
                value = "remote_was_deleted",
                nulls = Nulls.SKIP
        )
        public Builder remoteWasDeleted(Optional<Boolean> remoteWasDeleted) {
            this.remoteWasDeleted = remoteWasDeleted;
            return this;
        }

        public Builder remoteWasDeleted(Boolean remoteWasDeleted) {
            this.remoteWasDeleted = Optional.of(remoteWasDeleted);
            return this;
        }

        @JsonSetter(
                value = "is_commonly_used_as_team",
                nulls = Nulls.SKIP
        )
        public Builder isCommonlyUsedAsTeam(Optional<Boolean> isCommonlyUsedAsTeam) {
            this.isCommonlyUsedAsTeam = isCommonlyUsedAsTeam;
            return this;
        }

        public Builder isCommonlyUsedAsTeam(Boolean isCommonlyUsedAsTeam) {
            this.isCommonlyUsedAsTeam = Optional.of(isCommonlyUsedAsTeam);
            return this;
        }

        @JsonSetter(
                value = "field_mappings",
                nulls = Nulls.SKIP
        )
        public Builder fieldMappings(Optional<Map<String, JsonNode>> fieldMappings) {
            this.fieldMappings = fieldMappings;
            return this;
        }

        public Builder fieldMappings(Map<String, JsonNode> fieldMappings) {
            this.fieldMappings = Optional.of(fieldMappings);
            return this;
        }

        @JsonSetter(
                value = "remote_data",
                nulls = Nulls.SKIP
        )
        public Builder remoteData(Optional<List<RemoteData>> remoteData) {
            this.remoteData = remoteData;
            return this;
        }

        public Builder remoteData(List<RemoteData> remoteData) {
            this.remoteData = Optional.of(remoteData);
            return this;
        }

        public Group build() {
            return new Group(this.id, this.remoteId, this.createdAt, this.modifiedAt, this.parentGroup, this.name, this.type, this.remoteWasDeleted, this.isCommonlyUsedAsTeam, this.fieldMappings, this.remoteData, this.additionalProperties);
        }
    }
}
