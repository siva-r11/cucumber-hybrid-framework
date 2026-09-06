package com.framework.utils;

import com.framework.models.User;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonUtilsTest {

    @Test
    void serializesAndDeserializesUserJson() {
        User source = User.builder().name("Ada Lovelace").job("Engineer").build();

        String json = JsonUtils.toString(source);
        User result = JsonUtils.fromString(json, User.class);

        assertThat(json).contains("\n").contains("Ada Lovelace");
        assertThat(result.getName()).isEqualTo("Ada Lovelace");
        assertThat(result.getJob()).isEqualTo("Engineer");
    }

    @Test
    void ignoresUnknownPropertiesWhenDeserializing() {
        User result = JsonUtils.fromString("{\"name\":\"Grace\",\"unknown\":true}", User.class);

        assertThat(result.getName()).isEqualTo("Grace");
    }

    @Test
    void readsObjectListsAndRawFiles() throws Exception {
        Path file = Files.createTempFile("users", ".json");
        Files.writeString(file, "[{\"name\":\"Ada\"},{\"name\":\"Grace\"}]");

        List<User> users = JsonUtils.listFromFile(file.toString(), User.class);
        String raw = JsonUtils.readRaw(file.toString());

        assertThat(users).extracting(User::getName).containsExactly("Ada", "Grace");
        assertThat(raw).contains("\"name\":\"Ada\"");
        Files.deleteIfExists(file);
    }

    @Test
    void wrapsInvalidJsonAndMissingFileErrors() {
        assertThatThrownBy(() -> JsonUtils.fromString("not-json", User.class))
                .isInstanceOf(java.io.UncheckedIOException.class)
                .hasMessageContaining("Failed to deserialize JSON");
        assertThatThrownBy(() -> JsonUtils.readRaw("missing-file.json"))
                .isInstanceOf(java.io.UncheckedIOException.class)
                .hasMessageContaining("Failed to read file");
    }
}