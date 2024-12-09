package com.aguasfluentessa.pressuremonitor.repository;

import com.aguasfluentessa.pressuremonitor.config.BaseTestConfig;
import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PressureNotificationRepositoryTest extends BaseTestConfig {

    @Autowired
    private PressureNotificationRepository pressureNotificationRepository;

    @Test
    public void testSaveAndFindPressureNotification() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        PressureNotification notification = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false,
                now
        );

        PressureNotification savedNotification = pressureNotificationRepository.save(notification);
        assertNotNull(savedNotification.getId());

        Optional<PressureNotification> foundNotification = pressureNotificationRepository.findById(savedNotification.getId());
        assertTrue(foundNotification.isPresent());
        assertEquals("unique123", foundNotification.get().getGaugeUniqueIdentificator());
        assertEquals(25.0, foundNotification.get().getPressure());
        assertEquals(AlertLevel.HIGH, foundNotification.get().getAlertLevel());
        assertEquals(AlertType.HIGH_PRESSURE, foundNotification.get().getAlertType());
        assertFalse(foundNotification.get().getAcknowledged());
        assertEquals(now, foundNotification.get().getReferenceDateTime());
    }

    @Test
    public void testFindByFilters() {
        LocalDateTime now = LocalDateTime.now();
        PressureNotification notification1 = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false,
                now
        );

        PressureNotification notification2 = new PressureNotification(
                "unique456",
                30.0,
                AlertLevel.CRITICAL,
                AlertType.LEAK,
                true,
                now.minusDays(1)
        );

        pressureNotificationRepository.save(notification1);
        pressureNotificationRepository.save(notification2);

        List<PressureNotification> notifications = pressureNotificationRepository.findByFilters(
                now.minusDays(2),
                now,
                null,
                null,
                null,
                null
        );

        assertEquals(2, notifications.size());

        notifications = pressureNotificationRepository.findByFilters(
                now.minusDays(2),
                now,
                false,
                null,
                null,
                null
        );

        assertEquals(1, notifications.size());
        assertEquals("unique123", notifications.get(0).getGaugeUniqueIdentificator());

        notifications = pressureNotificationRepository.findByFilters(
                now.minusDays(2),
                now,
                true,
                "unique456",
                AlertLevel.CRITICAL,
                AlertType.LEAK
        );

        assertEquals(1, notifications.size());
        assertEquals("unique456", notifications.get(0).getGaugeUniqueIdentificator());
    }
}