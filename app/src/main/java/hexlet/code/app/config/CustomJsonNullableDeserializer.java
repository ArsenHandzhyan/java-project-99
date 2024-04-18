package hexlet.code.app.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.IOException;

public class CustomJsonNullableDeserializer extends JsonDeserializer<JsonNullable<String>> {

    @Override
    public JsonNullable<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        return value != null ? JsonNullable.of(value) : JsonNullable.undefined();
    }
}
