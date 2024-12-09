package com.aguasfluentessa.pressuremonitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PressureProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PressureProcessor.class);
    private static final double MIN_PRESSURE = 15.0;
    private static final double MAX_PRESSURE = 30.0;
    private static final double PRESSURE_VARIATION_THRESHOLD = 5.0;

    public void processPressure(List<Double> readings) {
        if (readings == null || readings.isEmpty()) {
            return;
        }

        double latestPressure = readings.get(0);
        double previousPressure = readings.size() > 1 ? readings.get(1) : latestPressure;

        if (Math.abs(latestPressure - previousPressure) >= PRESSURE_VARIATION_THRESHOLD) {
            generateImmediateAlert(latestPressure, previousPressure);
        }

        if (latestPressure < MIN_PRESSURE || latestPressure > MAX_PRESSURE) {
            generatePressureOutOfRangeAlert(latestPressure);
        }

        analyzePredictivePressure(readings);
    }

    private void generateImmediateAlert(double latestPressure, double previousPressure) {
        logger.warn("Immediate alert: Pressure variation detected. Latest pressure = {}, Previous pressure = {}",
                latestPressure, previousPressure);

    }

    private void generatePressureOutOfRangeAlert(double pressure) {
        logger.warn("Pressure out of range alert: Pressure = {}", pressure);
    }

    private void analyzePredictivePressure(List<Double> readings) {
        double averagePressure = readings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        logger.info("Predictive analysis: Average pressure = {}", averagePressure);
    }
}