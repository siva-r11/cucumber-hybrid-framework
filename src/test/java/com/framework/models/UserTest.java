package com.framework.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void builderCreatesUserWithSuppliedFields() {
        User user = User.builder()
                .id("7")
                .name("Ada Lovelace")
                .job("Engineer")
                .email("ada@example.com")
                .build();

        assertThat(user.getId()).isEqualTo("7");
        assertThat(user.getName()).isEqualTo("Ada Lovelace");
        assertThat(user.getJob()).isEqualTo("Engineer");
        assertThat(user.getEmail()).isEqualTo("ada@example.com");
    }

    @Test
    void supportsNoArgConstructionAndMutation() {
        User user = new User();

        user.setName("Grace Hopper");

        assertThat(user.getName()).isEqualTo("Grace Hopper");
        assertThat(user.getJob()).isNull();
    }
}