package com.aguasfluentessa.pressuremonitor.exceptions;

public class PressureGaugeNotFoundException extends RuntimeException {

    public PressureGaugeNotFoundException(String message) {
        super(message);
    }   
}