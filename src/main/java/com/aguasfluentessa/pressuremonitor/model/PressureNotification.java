package com.aguasfluentessa.pressuremonitor.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "pressure_notifications")
public class PressureNotification {
    
    @Id
    @SequenceGenerator(name = "pressure_notification_id_seq", sequenceName = "pressure_notification_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pressure_notification_id_seq")
    private Long id;

    @Column(nullable = false)
    private String gaugeUniqueIdentificator;
    
    @Column(nullable = false)
    private Double pressure;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private AlertLevel alertLevel;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private AlertType alertType;

    @Column(nullable = false)
    private Boolean acknowledged; 

    @Column(name = "reference_date_time", nullable = false)
    private LocalDateTime referenceDateTime = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public PressureNotification() {
    }
    
    public PressureNotification(Long id, String gaugeUniqueIdentificator, Double pressure, AlertLevel alertLevel,
            AlertType alertType, Boolean acknowledged, LocalDateTime referenLocalDateTime, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.gaugeUniqueIdentificator = gaugeUniqueIdentificator;
        this.pressure = pressure;
        this.alertLevel = alertLevel;
        this.alertType = alertType;
        this.acknowledged = acknowledged;
        this.referenceDateTime = referenLocalDateTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PressureNotification(String gaugeUniqueIdentificator, Double pressure, AlertLevel alertLevel,
            AlertType alertType, Boolean acknowledged, LocalDateTime referenceDateTime) {
        this.gaugeUniqueIdentificator = gaugeUniqueIdentificator;
        this.pressure = pressure;
        this.alertLevel = alertLevel;
        this.alertType = alertType;
        this.acknowledged = acknowledged;
        this.referenceDateTime = referenceDateTime;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public AlertLevel getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(AlertLevel alertLevel) {
        this.alertLevel = alertLevel;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public Boolean getAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(Boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public LocalDateTime getReferenceDateTime() {
        return referenceDateTime;
    }

    public void setReferenceDateTime(LocalDateTime referenceDateTime) {
        this.referenceDateTime = referenceDateTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    
        @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PressureNotification that = (PressureNotification) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(gaugeUniqueIdentificator, that.gaugeUniqueIdentificator) &&
                Objects.equals(pressure, that.pressure) &&
                alertLevel == that.alertLevel &&
                alertType == that.alertType &&
                Objects.equals(acknowledged, that.acknowledged) &&
                Objects.equals(referenceDateTime != null ? referenceDateTime.truncatedTo(ChronoUnit.SECONDS) : null, that.referenceDateTime != null ? that.referenceDateTime.truncatedTo(ChronoUnit.SECONDS) : null) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gaugeUniqueIdentificator, pressure, alertLevel, alertType, acknowledged, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "PressureNotification [id=" + id + ", gaugeUniqueIdentificator=" + gaugeUniqueIdentificator
                + ", pressure=" + pressure + ", alertLevel=" + alertLevel + ", alertType=" + alertType
                + ", acknowledged=" + acknowledged + ", referenceDateTime=" + referenceDateTime + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
   
}
