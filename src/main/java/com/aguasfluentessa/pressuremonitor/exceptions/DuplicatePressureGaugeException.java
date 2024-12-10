package com.aguasfluentessa.pressuremonitor.exceptions;

public class DuplicatePressureGaugeException extends RuntimeException {
    public DuplicatePressureGaugeException(String message) {
        super(message);
    }
}