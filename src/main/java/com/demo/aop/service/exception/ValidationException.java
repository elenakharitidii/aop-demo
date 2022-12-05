package com.demo.aop.service.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String error) {
        this(error, null);
    }
    public ValidationException(String error, Throwable cause) {
        super(error, cause);
    }
}
