package fr.emcastro.jdbcretyper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple wrapper for JSON content. Used to demonstrate type transformation
 * between application-level JSON objects and JDBC VARCHAR/JSON columns.
 */
public class JsonBox {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String json;

    public JsonBox(String json) {
        this.json = json;
    }

    /**
     * Creates a JsonBox from an object by serializing it to JSON.
     */
    public static JsonBox fromObject(Object obj) {
        try {
            return new JsonBox(MAPPER.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    public String getJson() {
        return json;
    }

    /**
     * Parses the JSON content into the given type.
     */
    public <T> T toObject(Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to " + type.getName(), e);
        }
    }

    @Override
    public String toString() {
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonBox jsonBox = (JsonBox) o;
        return json != null ? json.equals(jsonBox.json) : jsonBox.json == null;
    }

    @Override
    public int hashCode() {
        return json != null ? json.hashCode() : 0;
    }
}
