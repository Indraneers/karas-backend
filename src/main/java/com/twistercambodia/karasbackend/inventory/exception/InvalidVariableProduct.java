package com.twistercambodia.karasbackend.inventory.exception;

public class InvalidVariableProduct extends RuntimeException {
    private String message;

    public InvalidVariableProduct() {}

    public InvalidVariableProduct(String message) {
        super(message);
        this.message = "Variable product name cannot be empty";
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
