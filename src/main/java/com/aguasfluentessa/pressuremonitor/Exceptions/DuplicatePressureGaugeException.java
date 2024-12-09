package com.aguasfluentessa.pressuremonitor.Exceptions;

public class DuplicatePressureGaugeException extends RuntimeException {
    public DuplicatePressureGaugeException(String message) {
        super(message);
    }
}