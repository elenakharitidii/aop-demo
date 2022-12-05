package com.demo.aop.service;

import com.demo.aop.service.exception.ValidationException;
import com.demo.aop.service.model.CreateUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

public class CreateUserTest {
    static Logger logger = Logger.getLogger(CreateUserTest.class.getSimpleName());
    @Test
    public void throw_validationException_if_email_is_not_valid() {
        Assertions.assertThrows(
                ValidationException.class,
                () -> CreateUser.builder()
                        .name("Whiskey Jack")
                        .email("meme")
                        .build());
    }
}
