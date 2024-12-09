package com.aguasfluentessa.pressuremonitor.model;

public class PressureNotificationEnums {

    public enum AlertLevel {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum AlertType {
        LEAK,
        HIGH_PRESSURE,
        LOW_PRESSURE,
        OTHER
    }
}
