package com.twistercambodia.karasbackend.vehicle.exception;

public class VehicleNotFoundException extends RuntimeException {
    private String message;

    public VehicleNotFoundException() {}

    public VehicleNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
