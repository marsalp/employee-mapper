package com.mapper.employee_mapper.merge_sdk;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

class DateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
    private static final SimpleModule MODULE = (new SimpleModule()).addDeserializer(OffsetDateTime.class, new DateTimeDeserializer());

    DateTimeDeserializer() {
    }

    public static SimpleModule getModule() {
        return MODULE;
    }

    public OffsetDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken token = parser.currentToken();
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return OffsetDateTime.ofInstant(Instant.ofEpochSecond(parser.getValueAsLong()), ZoneOffset.UTC);
        } else {
            TemporalAccessor temporal = DateTimeFormatter.ISO_DATE_TIME.parseBest(parser.getValueAsString(), OffsetDateTime::from, LocalDateTime::from);
            return temporal.query(TemporalQueries.offset()) == null ? LocalDateTime.from(temporal).atOffset(ZoneOffset.UTC) : OffsetDateTime.from(temporal);
        }
    }
}
