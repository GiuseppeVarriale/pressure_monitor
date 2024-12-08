package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class InMemoryPressureGaugeStoreService {

    private static final int MAX_READINGS = 10;
    private final ConcurrentMap<String, Deque<PressureGaugeReading>> readingsMap = new ConcurrentHashMap<>();

    public void addReading(PressureGaugeReading reading) {
        readingsMap.computeIfAbsent(reading.getGaugeUniqueIdentificator(), k -> new ArrayDeque<>()).add(reading);
        Deque<PressureGaugeReading> readings = readingsMap.get(reading.getGaugeUniqueIdentificator());
        if (readings.size() > MAX_READINGS) {
            readings.poll();
        }
    }

    public Deque<PressureGaugeReading> getReadings(String gaugeUniqueIdentificator) {
        return readingsMap.getOrDefault(gaugeUniqueIdentificator, new ArrayDeque<>());
    }
}