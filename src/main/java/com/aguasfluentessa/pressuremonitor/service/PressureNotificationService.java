package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;
import com.aguasfluentessa.pressuremonitor.repository.PressureNotificationRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PressureNotificationService {

    @Autowired
    private PressureNotificationRepository pressureNotificationRepository;

    public List<PressureNotification> findByFilters(LocalDateTime startDate, LocalDateTime endDate,
            Boolean acknowledged,
            String gaugeUniqueIdentificator, AlertLevel alertLevel, AlertType alertType) {
        if (startDate == null && endDate == null && acknowledged == null && gaugeUniqueIdentificator == null
                && alertLevel == null && alertType == null) {
            return pressureNotificationRepository.findAll();
        }
        return pressureNotificationRepository.findByFilters(startDate, endDate, acknowledged, gaugeUniqueIdentificator,
                alertLevel, alertType);
    }

    public void setAcknowledged(Long notificationId) {
        PressureNotification notification = pressureNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notification.setAcknowledged(true);
        pressureNotificationRepository.save(notification);
    }
}