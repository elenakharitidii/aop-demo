package com.demo.aop.aspects;

import com.demo.aop.repository.RepositoryException;
import com.demo.aop.rest.user.ErrorResponse;
import com.demo.aop.service.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(annotations = UseControllerAdvice.class)
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        Object errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                exception.getMessage(),
                "todo" // TODO: get path from request
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException exception,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(
            RepositoryException exception,
            WebRequest request
    ) {
        logger.error("event=failedToFindRecourse", exception);
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            RuntimeException exception,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                exception.getMessage(),
                path(request)
        );
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
    private String path(WebRequest request) {
        String description = request.getDescription(false);
        if (description.startsWith("uri=")) return description.substring("uri=".length());
        return description;
    }
}