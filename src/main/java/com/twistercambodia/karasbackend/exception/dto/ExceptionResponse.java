package com.twistercambodia.karasbackend.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public class ExceptionResponse {
    private int statusCode;
    private ExceptionType code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> fieldErrors;

    public ExceptionResponse(int statusCode, ExceptionType exceptionType, String message) {
        this(statusCode, exceptionType, message, null);
    }

    public ExceptionResponse(int statusCode, ExceptionType exceptionType, String message, Map<String, String> fieldErrors) {
        this.statusCode = statusCode;
        this.code = exceptionType;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ExceptionType getCode() {
        return code;
    }

    public void setCode(ExceptionType code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}

