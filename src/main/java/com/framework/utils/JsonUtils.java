package com.framework.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Thin wrapper over a single, configured Jackson {@link ObjectMapper}.
 *
 * <p>Reusing one mapper is a Jackson best practice (they are expensive to build and thread-safe
 * once configured). Unknown-property tolerance keeps deserialization resilient when APIs add
 * fields we do not model.
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private JsonUtils() {
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }

    /** Deserialize a JSON string into the given type. */
    public static <T> T fromString(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to deserialize JSON to " + type.getSimpleName(), e);
        }
    }

    /** Deserialize a JSON file into the given type. */
    public static <T> T fromFile(String filePath, Class<T> type) {
        try {
            return MAPPER.readValue(new File(filePath), type);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read JSON file: " + filePath, e);
        }
    }

    /** Deserialize a JSON array file into a typed List. */
    public static <T> List<T> listFromFile(String filePath, Class<T> elementType) {
        try {
            var listType = MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
            return MAPPER.readValue(new File(filePath), listType);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read JSON list file: " + filePath, e);
        }
    }

    /** Serialize any object to a pretty-printed JSON string. */
    public static String toString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to serialize object to JSON", e);
        }
    }

    /** Read a raw JSON file as a String (useful for schemas / templates). */
    public static String readRaw(String filePath) {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read file: " + filePath, e);
        }
    }
}
