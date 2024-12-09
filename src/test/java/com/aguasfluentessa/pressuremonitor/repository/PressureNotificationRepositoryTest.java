package com.aguasfluentessa.pressuremonitor.repository;


import com.aguasfluentessa.pressuremonitor.config.BaseTestConfig;
import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;
import com.aguasfluentessa.pressuremonitor.repository.PressureNotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class PressureNotificationRepositoryTest extends BaseTestConfig{

    @Autowired
    private PressureNotificationRepository pressureNotificationRepository;

    @Test
    public void testSaveAndFindPressureNotification() {
        PressureNotification notification = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false
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
    }
}