package com.aguasfluentessa.pressuremonitor.repository;

import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PressureNotificationRepository extends JpaRepository<PressureNotification, Long> {
}