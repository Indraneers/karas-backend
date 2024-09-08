package com.twistercambodia.karasbackend.inventory.exception;

public class CategoryNotFoundException extends RuntimeException {
    private String message;

    public CategoryNotFoundException() {}

    public CategoryNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
