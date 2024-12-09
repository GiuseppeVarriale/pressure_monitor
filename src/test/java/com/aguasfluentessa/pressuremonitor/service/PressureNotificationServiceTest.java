package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;
import com.aguasfluentessa.pressuremonitor.repository.PressureNotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class PressureNotificationServiceTest {

    @Mock
    private PressureNotificationRepository pressureNotificationRepository;

    @InjectMocks
    private PressureNotificationService pressureNotificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByFilters() {
        PressureNotification notification = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false,
                LocalDateTime.now()
        );

        List<PressureNotification> expectedNotifications = Arrays.asList(notification);
        when(pressureNotificationRepository.findByFilters(any(), any(), any(), any(), any(), any()))
                .thenReturn(expectedNotifications);

        List<PressureNotification> actualNotifications = pressureNotificationService.findByFilters(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                false,
                "unique123",
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE
        );

        assertEquals(expectedNotifications, actualNotifications);
        verify(pressureNotificationRepository, times(1)).findByFilters(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testSetAcknowledged() {
        PressureNotification notification = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false,
                LocalDateTime.now()
        );
        notification.setId(1L);

        when(pressureNotificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        pressureNotificationService.setAcknowledged(1L);

        assertTrue(notification.getAcknowledged());
        verify(pressureNotificationRepository, times(1)).findById(1L);
        verify(pressureNotificationRepository, times(1)).save(notification);
    }

    @Test
    public void testSetAcknowledgedThrowsEntityNotFoundException() {
        when(pressureNotificationRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            pressureNotificationService.setAcknowledged(1L);
        });

        assertEquals("Notification not found", exception.getMessage());
        verify(pressureNotificationRepository, times(1)).findById(1L);
        verify(pressureNotificationRepository, times(0)).save(any());
    }
}
