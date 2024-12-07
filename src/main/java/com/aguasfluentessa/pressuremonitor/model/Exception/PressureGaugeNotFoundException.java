package com.aguasfluentessa.pressuremonitor.model.Exception;

public class PressureGaugeNotFoundException extends RuntimeException {

    public PressureGaugeNotFoundException(String message) {
        super(message);
    }   
}