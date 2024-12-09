package com.aguasfluentessa.pressuremonitor.Exceptions;

public class PressureGaugeNotFoundException extends RuntimeException {

    public PressureGaugeNotFoundException(String message) {
        super(message);
    }   
}