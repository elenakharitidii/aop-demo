package com.demo.aop.repository;

import lombok.ToString;

@ToString
public class RepositoryException extends RuntimeException {
    private RepositoryError error;

    public RepositoryException(RepositoryError error, String message) {
        this(error, message, null);
    }
    public RepositoryException(RepositoryError error, String message, Throwable cause) {
        super(error+":"+message, cause);
        this.error = error;
    }

    public enum RepositoryError {
        CONFLICT,
        NOT_FOUND,
        INVALID
    }

}

