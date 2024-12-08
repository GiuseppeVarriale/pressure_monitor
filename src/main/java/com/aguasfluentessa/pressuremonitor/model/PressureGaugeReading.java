package com.aguasfluentessa.pressuremonitor.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "pressure_gauge_readings")
public class PressureGaugeReading {

    @Id
    @SequenceGenerator(name = "pressure_gauge_reading_id_seq", sequenceName = "pressure_gauge_reading_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pressure_gauge_reading_id_seq")
    private Long id;

    @Column(name = "pressure_gauge_gauge_unique_id", nullable = false)
    private String gaugeUniqueIdentificator;
    
    @Column(nullable = false)
    private Double pressure;
    
    @Column(nullable = false)
    private String systemId;
    
    @Column(nullable = false)
    private Double measureLat;

    @Column(nullable = false)
    private Double measureLong;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PressureGaugeReading() {
    }

    public PressureGaugeReading(String gaugeUniqueIdentificator, Double pressure, String systemId, Double measureLat, Double measureLong) {
        this.gaugeUniqueIdentificator = gaugeUniqueIdentificator;
        this.pressure = pressure;
        this.systemId = systemId;
        this.measureLat = measureLat;
        this.measureLong = measureLong;
    }

    public PressureGaugeReading(Long id, String gaugeUniqueIdentificator, Double pressure, String systemId, Double measureLat,
            Double measureLong, LocalDateTime createdAt) {
        this.id = id;
        this.gaugeUniqueIdentificator = gaugeUniqueIdentificator;
        this.pressure = pressure;
        this.systemId = systemId;
        this.measureLat = measureLat;
        this.measureLong = measureLong;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getGaugeUniqueIdentificator() {
        return gaugeUniqueIdentificator;
    }

    public Double getPressure() {
        return pressure;
    }

    public String getSystemId() {
        return systemId;
    }

    public Double getMeasureLat() {
        return measureLat;
    }

    public Double getMeasureLong() {
        return measureLong;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setGaugeUniqueIdentificator(String gaugeUniqueIdentificator) {
        this.gaugeUniqueIdentificator = gaugeUniqueIdentificator;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setMeasureLat(Double measureLat) {
        this.measureLat = measureLat;
    }

    public void setMeasureLong(Double measureLong) {
        this.measureLong = measureLong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PressureGaugeReading that = (PressureGaugeReading) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(gaugeUniqueIdentificator, that.gaugeUniqueIdentificator) &&
                Objects.equals(pressure, that.pressure) &&
                Objects.equals(systemId, that.systemId) &&
                Objects.equals(measureLat, that.measureLat) &&
                Objects.equals(measureLong, that.measureLong) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gaugeUniqueIdentificator, pressure, systemId, measureLat, measureLong, createdAt);
    }

    @Override
    public String toString() {
        return "PressureGaugeReading{" +
                "id=" + id +
                ", gaugeUniqueIdentificator='" + gaugeUniqueIdentificator + '\'' +
                ", pressure=" + pressure +
                ", systemId='" + systemId + '\'' +
                ", measureLat=" + measureLat +
                ", measureLong=" + measureLong +
                ", createdAt=" + createdAt +
                '}';
    }
}