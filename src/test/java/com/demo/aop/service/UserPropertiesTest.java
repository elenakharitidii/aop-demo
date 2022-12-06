package com.demo.aop.service;

import com.demo.aop.service.exception.ValidationException;
import com.demo.aop.service.model.UserProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

public class UserPropertiesTest {
    static Logger logger = Logger.getLogger(UserPropertiesTest.class.getSimpleName());
    @Test
    public void throw_validationException_if_email_is_not_valid() {
        Assertions.assertThrows(
                ValidationException.class,
                () -> UserProperties.builder()
                        .name("Whiskey Jack")
                        .email("meme")
                        .build());
    }
}
