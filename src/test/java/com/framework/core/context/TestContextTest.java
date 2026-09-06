package com.framework.core.context;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestContextTest {

    @Test
    void createsAnEmptyScenarioContext() {
        TestContext context = new TestContext();

        assertThat(context.getScenarioContext()).isNotNull();
        assertThat(context.getScenarioContext().contains("missing")).isFalse();
    }
}