package com.twistercambodia.karasbackend.inventory.exception;

public class ProductNotFoundException extends RuntimeException {
    private String message;

    public ProductNotFoundException() {}

    public ProductNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
