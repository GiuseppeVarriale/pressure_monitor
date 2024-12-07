package com.aguasfluentessa.pressuremonitor.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "pressure_gauge")
public class PressureGauge {

    @Id
    @SequenceGenerator(name = "pressure_gauge_id_seq", sequenceName = "pressure_gauge_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pressure_gauge_id_seq")
    private Long id;

    @Column(unique = true)
    private String systemId;

    @Column(unique = true)
    private String gaugeUniqueIdentificator;

    private Double lat;
    private Double lon;
    private Boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public PressureGauge() {
    }

    public PressureGauge(Long id, String systemId,
            String gaugeUniqueIdentificator,
            Double lat, 
            Double lon,
            Boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.systemId = systemId;
        this.gaugeUniqueIdentificator = gaugeUniqueIdentificator;
        this.lat = lat;
        this.lon = lon;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PressureGauge(String systemId,
            String gaugeUniqueIdentificator,
            Double lat,
            Double lon,
            Boolean active) {
        this.systemId = systemId;
        this.gaugeUniqueIdentificator = gaugeUniqueIdentificator;
        this.lat = lat;
        this.lon = lon;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setGaugeUniqueIdentificator(String gaugeUniqueIdentificator) {
        this.gaugeUniqueIdentificator = gaugeUniqueIdentificator;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "PressureGauge [id=" + id + ", systemId=" + systemId + ", gaugeUniqueIdentificator="
                + gaugeUniqueIdentificator + ", lat=" + lat + ", lon=" + lon + ", active=" + active + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}