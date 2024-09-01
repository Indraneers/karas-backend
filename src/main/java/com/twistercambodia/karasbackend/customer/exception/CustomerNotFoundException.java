package com.twistercambodia.karasbackend.customer.exception;

public class CustomerNotFoundException extends RuntimeException {
    private String message;

    public CustomerNotFoundException() {}

    public CustomerNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}