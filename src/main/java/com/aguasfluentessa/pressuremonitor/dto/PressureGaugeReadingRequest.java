package com.aguasfluentessa.pressuremonitor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PressureGaugeReadingRequest {

    @NotBlank(message = "Gauge Unique Identificator cannot be blank")
    private String gaugeUniqueIdentificator;

    @NotNull(message = "Pressure cannot be null")
    private Double pressure;

    public String getGaugeUniqueIdentificator() {
        return gaugeUniqueIdentificator;
    }

    public void setGaugeUniqueIdentificator(String gaugeUniqueIdentificator) {
        this.gaugeUniqueIdentificator = gaugeUniqueIdentificator;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }
}