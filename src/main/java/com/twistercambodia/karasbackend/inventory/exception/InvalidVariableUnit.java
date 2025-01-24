package com.twistercambodia.karasbackend.inventory.exception;

public class InvalidVariableUnit extends RuntimeException {
    private String message;

    public InvalidVariableUnit() {}

    public InvalidVariableUnit(String message) {
        super(message);
        this.message = "Variable unit toBaseUnit cannot be empty";
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
