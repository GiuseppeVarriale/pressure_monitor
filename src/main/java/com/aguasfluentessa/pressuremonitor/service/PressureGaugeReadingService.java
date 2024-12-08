package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;
import com.aguasfluentessa.pressuremonitor.model.Exception.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeReadingRepository;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.List;

@Service
public class PressureGaugeReadingService {

    @Autowired
    private PressureGaugeReadingRepository pressureGaugeReadingRepository;

    @Autowired
    private PressureGaugeRepository pressureGaugeRepository;

    @Autowired
    private InMemoryPressureGaugeStoreService inMemoryPressureGaugeStore;

    public List<PressureGaugeReading> findAll() {
        return pressureGaugeReadingRepository.findAll();
    }

    public List<PressureGaugeReading> findByFilters(LocalDateTime startDate, LocalDateTime endDate, Double minPressure,
            Double maxPressure) {
        if (startDate == null && endDate == null && minPressure == null && maxPressure == null) {
            return findAll();
        }
        return pressureGaugeReadingRepository.findByFilters(startDate, endDate, minPressure, maxPressure);
    }

    public PressureGaugeReading save(String gaugeUniqueIdentificator, Double pressure) {
        PressureGauge pressureGauge = pressureGaugeRepository.findByGaugeUniqueIdentificator(gaugeUniqueIdentificator);
        if (pressureGauge == null) {
            throw new PressureGaugeNotFoundException(
                    "Pressure Gauge not found for gaugeUniqueIdentificator: " + gaugeUniqueIdentificator);
        }
        PressureGaugeReading reading = new PressureGaugeReading(
                gaugeUniqueIdentificator,
                pressure,
                pressureGauge.getSystemId(),
                pressureGauge.getLat(),
                pressureGauge.getLon());

        PressureGaugeReading savedReading = pressureGaugeReadingRepository.save(reading);
        inMemoryPressureGaugeStore.addReading(savedReading);
        return savedReading;
    }

    public Deque<PressureGaugeReading> getLastReadingsInMemory(String gaugeUniqueIdentificator) {
        return inMemoryPressureGaugeStore.getReadings(gaugeUniqueIdentificator);
    }

}