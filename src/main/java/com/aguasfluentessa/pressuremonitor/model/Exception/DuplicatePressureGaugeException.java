package com.aguasfluentessa.pressuremonitor.model.Exception;

public class DuplicatePressureGaugeException extends RuntimeException {
    public DuplicatePressureGaugeException(String message) {
        super(message);
    }
}