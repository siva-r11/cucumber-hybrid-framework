package com.framework.utils;

import com.framework.models.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestDataProviderTest {

    @Test
    void loadsKnownUserFixture() {
        User user = TestDataProvider.userFixture("newUser");

        assertThat(user.getName()).isEqualTo("Ada Lovelace");
        assertThat(user.getJob()).isEqualTo("Automation Engineer");
    }

    @Test
    void rejectsUnknownUserFixture() {
        assertThatThrownBy(() -> TestDataProvider.userFixture("doesNotExist"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No user fixture named 'doesNotExist'");
    }

    @Test
    void generatesPopulatedRandomUser() {
        User user = TestDataProvider.randomUser();

        assertThat(user.getName()).isNotBlank();
        assertThat(user.getJob()).isNotBlank();
        assertThat(user.getEmail()).isNotBlank();
    }
}