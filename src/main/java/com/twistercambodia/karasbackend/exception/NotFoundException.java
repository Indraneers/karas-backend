package com.twistercambodia.karasbackend.exception;

public class NotFoundException extends RuntimeException {
    private String message;

    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
