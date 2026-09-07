package com.framework.utils;

import com.framework.models.RegistrationData;
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

    @Test
    void generatesUniqueParaBankRegistrationData() {
        RegistrationData first = TestDataProvider.generateRegistrationData();
        RegistrationData second = TestDataProvider.generateRegistrationData();

        assertThat(first.firstName()).isEqualTo("Jane");
        assertThat(first.lastName()).isEqualTo("Doe");
        assertThat(first.street()).isEqualTo("123 Main St");
        assertThat(first.city()).isEqualTo("Springfield");
        assertThat(first.state()).isEqualTo("IL");
        assertThat(first.zipCode()).isEqualTo("62704");
        assertThat(first.phone()).isEqualTo("555-0100");
        assertThat(first.ssn()).matches("\\d{3}-\\d{2}-\\d{4}");
        assertThat(first.username()).startsWith("qauser").isNotEqualTo(second.username());
        assertThat(first.password()).isEqualTo("P@ssw0rd123");
    }
}