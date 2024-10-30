package com.twistercambodia.karasbackend.exception.dto;

import java.util.ArrayList;
import java.util.List;

public class ExceptionResponse {
    private int statusCode;
    private ExceptionType code;
    private String message;

    public ExceptionResponse(int statusCode, ExceptionType exceptionType, String message) {
        this.statusCode = statusCode;
        this.code = exceptionType;
        this.message = message;
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
}

