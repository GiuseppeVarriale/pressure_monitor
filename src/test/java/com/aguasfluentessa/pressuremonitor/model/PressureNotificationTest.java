package com.aguasfluentessa.pressuremonitor.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;

public class PressureNotificationTest {
    @Test
    public void testEqualsAndHashCode() {
        PressureNotification notification1 = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false
        );

        PressureNotification notification2 = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false
        );

        assertEquals(notification1, notification2);
        assertEquals(notification1.hashCode(), notification2.hashCode());
    }

    @Test
    public void testToString() {
        PressureNotification notification = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false
        );

        String expectedString = "PressureNotification [id=null, gaugeUniqueIdentificator=unique123, pressure=25.0, alertLevel=HIGH, alertType=HIGH_PRESSURE, acknowledged=false, createdAt=null, updatedAt=null]";
        assertEquals(expectedString, notification.toString());
    }

    @Test
    public void testGettersAndSetters() {
        PressureNotification notification = new PressureNotification();
        notification.setId(1L);
        notification.setGaugeUniqueIdentificator("unique123");
        notification.setPressure(25.0);
        notification.setAlertLevel(AlertLevel.HIGH);
        notification.setAlertType(AlertType.HIGH_PRESSURE);
        notification.setAcknowledged(false);
        LocalDateTime now = LocalDateTime.now();
        notification.setCreatedAt(now);
        notification.setUpdatedAt(now);

        assertEquals(1L, notification.getId());
        assertEquals("unique123", notification.getGaugeUniqueIdentificator());
        assertEquals(25.0, notification.getPressure());
        assertEquals(AlertLevel.HIGH, notification.getAlertLevel());
        assertEquals(AlertType.HIGH_PRESSURE, notification.getAlertType());
        assertFalse(notification.getAcknowledged());
        assertEquals(now, notification.getCreatedAt());
        assertEquals(now, notification.getUpdatedAt());
    }
}
