package com.demo.aop.service.exception;

import java.util.UUID;

public class ErrorMessageFactory {
    public static String invalidEmail(String email) {
        return String.format("not a valid email: %s", email);
    }

    public static String userNotFound(UUID id) {
        return String.format("User with ID %s does not exist", id);
    }

    public static String invalidValue(String name, String value) {
        return String.format("Invalid value: %s provided for property: %s", value, name);
    }

    public static String userEmailExists(String email) {
        return String.format("Email value '%s' is not unique", email);
    }
}
