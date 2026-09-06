package com.framework.core.context;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScenarioContextTest {

    @Test
    void storesAndRetrievesValuesByKey() {
        ScenarioContext context = new ScenarioContext();

        context.set("userId", 42);

        assertThat(context.contains("userId")).isTrue();
        assertThat(context.<Integer>get("userId")).isEqualTo(42);
        assertThat(context.<Integer>getOptional("userId")).contains(42);
    }

    @Test
    void returnsEmptyOptionalForMissingKeyAndClearsValues() {
        ScenarioContext context = new ScenarioContext();
        context.set("token", "abc");

        context.clear();

        assertThat(context.contains("token")).isFalse();
        assertThat(context.<String>getOptional("token")).isEmpty();
        assertThat(context.<String>get("token")).isNull();
    }
}