package com.twistercambodia.karasbackend.inventory.exception;

public class UnitNotFoundException extends RuntimeException {
    private String message;

    public UnitNotFoundException() {}

    public UnitNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
