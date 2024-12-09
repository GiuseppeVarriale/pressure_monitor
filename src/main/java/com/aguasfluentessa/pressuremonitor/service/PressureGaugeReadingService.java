package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.Exceptions.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeReadingRepository;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PressureGaugeReadingService {

    @Autowired
    private PressureGaugeReadingRepository pressureGaugeReadingRepository;

    @Autowired
    private PressureGaugeRepository pressureGaugeRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private PressureGaugeReadingService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

        
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
        addReadingsToGauge(savedReading);
        addGaugeReadingToProcessQueue(savedReading);
        return savedReading;
    }

    private void addGaugeReadingToProcessQueue(PressureGaugeReading reading) {
        redisTemplate.opsForList().leftPush("pressureReadingsProcessQueue", reading.getGaugeUniqueIdentificator());
    }

    private void addReadingsToGauge(PressureGaugeReading reading) {
        String key = "GaugeMeasure_" + reading.getGaugeUniqueIdentificator();
        redisTemplate.opsForList().leftPush(key, reading.getPressure());
        redisTemplate.opsForList().trim(key, 0, 9);
    }

    public List<Double> getLastReadings(String gaugeUniqueIdentificator) {
        String key = "GaugeMeasure_" + gaugeUniqueIdentificator;
        List<Object> objects = redisTemplate.opsForList().range(key, 0, -1);
        if (objects == null) {
            return new ArrayList<>();
        }
        return objects.stream()
                      .map(obj -> (Double) obj)
                      .collect(Collectors.toList());
    }
}