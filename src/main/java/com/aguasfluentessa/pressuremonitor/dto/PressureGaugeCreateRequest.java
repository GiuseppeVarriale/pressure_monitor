package com.aguasfluentessa.pressuremonitor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PressureGaugeCreateRequest {
    
    @NotBlank
    private String systemId;

    @NotBlank
    private String gaugeUniqueIdentificator;

    @NotNull
    private Double lat;
    
    @NotNull
    private Double lon;
    
    @NotNull
    private Boolean active;

    public String getSystemId() {
        return systemId;
    }

    public String getGaugeUniqueIdentificator() {
        return gaugeUniqueIdentificator;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Boolean getActive() {
        return active;
    }

    

}
