package com.demo.aop.rest.user;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
}