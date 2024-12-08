package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryPressureGaugeStoreServiceTest {

    private InMemoryPressureGaugeStoreService inMemoryPressureGaugeStoreService;

    @BeforeEach
    public void setUp() {
        inMemoryPressureGaugeStoreService = new InMemoryPressureGaugeStoreService();
    }

    @Test
    public void testAddReading() {
        PressureGaugeReading reading = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);
        inMemoryPressureGaugeStoreService.addReading(reading);

        Deque<PressureGaugeReading> readings = inMemoryPressureGaugeStoreService.getReadings("unique123");
        assertEquals(1, readings.size());
        assertTrue(readings.contains(reading));
    }

    @Test
    public void testAddReading_MaxSize() {
        for (int i = 0; i < 15; i++) {
            PressureGaugeReading reading = new PressureGaugeReading("unique123", 100.0 + i, "system123", 12.34, 56.78);
            inMemoryPressureGaugeStoreService.addReading(reading);
        }

        Deque<PressureGaugeReading> readings = inMemoryPressureGaugeStoreService.getReadings("unique123");
        assertEquals(10, readings.size());
        assertEquals(105.0, readings.peekFirst().getPressure());
        assertEquals(114.0, readings.peekLast().getPressure());
    }

    @Test
    public void testGetReadings_NotFound() {
        Deque<PressureGaugeReading> readings = inMemoryPressureGaugeStoreService.getReadings("nonexistent");
        assertTrue(readings.isEmpty());
    }
}