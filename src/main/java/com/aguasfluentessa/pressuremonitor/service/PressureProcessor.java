package com.aguasfluentessa.pressuremonitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;
import com.aguasfluentessa.pressuremonitor.repository.PressureNotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PressureProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PressureProcessor.class);
    private static final double MIN_PRESSURE = 15.0;
    private static final double MAX_PRESSURE = 30.0;
    private static final double PRESSURE_VARIATION_THRESHOLD = 5.0;

    @Autowired
    private PressureNotificationRepository pressureNotificationRepository;

    public void processPressure(List<Double> readings, String gaugeUniqueIdentificator) {
        if (readings == null || readings.isEmpty()) {
            return;
        }

        double latestPressure = readings.get(0);
        double previousPressure = readings.size() > 1 ? readings.get(1) : latestPressure;

        if (Math.abs(latestPressure - previousPressure) >= PRESSURE_VARIATION_THRESHOLD) {
            generateImmediateAlert(latestPressure, previousPressure, gaugeUniqueIdentificator);
        }

        if (latestPressure < MIN_PRESSURE || latestPressure > MAX_PRESSURE) {
            generatePressureOutOfRangeAlert(latestPressure, gaugeUniqueIdentificator);
        }
    }

    private void generateImmediateAlert(double latestPressure, double previousPressure,
            String gaugeUniqueIdentificator) {
        logger.warn("Immediate alert: Pressure variation detected. Latest pressure = {}, Previous pressure = {}",
                latestPressure, previousPressure);

        AlertType alertType = latestPressure < previousPressure ? AlertType.LEAK : AlertType.HIGH_PRESSURE;
        PressureNotification notification = new PressureNotification(
                gaugeUniqueIdentificator,
                latestPressure,
                AlertLevel.MEDIUM,
                alertType,
                false,
                LocalDateTime.now());

        pressureNotificationRepository.save(notification);

    }

    private void generatePressureOutOfRangeAlert(double pressure, String gaugeUniqueIdentificator) {
        logger.warn("Pressure out of range alert: Pressure = {}", pressure);
        AlertType alertType = pressure < MIN_PRESSURE ? AlertType.LEAK : AlertType.HIGH_PRESSURE;
        PressureNotification notification = new PressureNotification(
                gaugeUniqueIdentificator,
                pressure,
                AlertLevel.HIGH,
                alertType,
                false,
                LocalDateTime.now());

        pressureNotificationRepository.save(notification);

    }

}