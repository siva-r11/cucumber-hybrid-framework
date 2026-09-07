package com.framework.models;

public record RegistrationData(
        String firstName,
        String lastName,
        String street,
        String city,
        String state,
        String zipCode,
        String phone,
        String ssn,
        String username,
        String password) {
}