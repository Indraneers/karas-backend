package com.twistercambodia.karasbackend.autoService.exception;

public class AutoServiceNotFoundException extends RuntimeException {
    private String message;

    public AutoServiceNotFoundException() {}

    public AutoServiceNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
