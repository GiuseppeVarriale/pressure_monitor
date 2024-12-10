package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.exceptions.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeReadingRepository;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PressureGaugeReadingServiceTest {

    @Mock
    private PressureGaugeReadingRepository pressureGaugeReadingRepository;

    @Mock
    private PressureGaugeRepository pressureGaugeRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @InjectMocks
    private PressureGaugeReadingService pressureGaugeReadingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForList()).thenReturn(listOperations);
    }

    @Test
    public void testFindAll() {
        PressureGaugeReading reading1 = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);
        PressureGaugeReading reading2 = new PressureGaugeReading("unique456", 102.5, "system456", 23.45, 67.89);
        when(pressureGaugeReadingRepository.findAll()).thenReturn(Arrays.asList(reading1, reading2));

        List<PressureGaugeReading> readings = pressureGaugeReadingService.findAll();
        assertEquals(2, readings.size());
        assertTrue(readings.contains(reading1));
        assertTrue(readings.contains(reading2));
    }

    @Test
    public void testFindByFilters() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        PressureGaugeReading reading1 = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);
        PressureGaugeReading reading2 = new PressureGaugeReading("unique456", 102.5, "system456", 23.45, 67.89);
        when(pressureGaugeReadingRepository.findByFilters(startDate, endDate, 100.0, 103.0))
                .thenReturn(Arrays.asList(reading1, reading2));

        List<PressureGaugeReading> readings = pressureGaugeReadingService.findByFilters(startDate, endDate, 100.0,
                103.0);
        assertEquals(2, readings.size());
        assertTrue(readings.contains(reading1));
        assertTrue(readings.contains(reading2));
    }

    @Test
    public void testSave() {
        PressureGauge pressureGauge = new PressureGauge("system123", "unique123", 12.34, 56.78, true);
        when(pressureGaugeRepository.findByGaugeUniqueIdentificator("unique123")).thenReturn(pressureGauge);

        PressureGaugeReading reading = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);
        when(pressureGaugeReadingRepository.save(any(PressureGaugeReading.class))).thenReturn(reading);

        PressureGaugeReading savedReading = pressureGaugeReadingService.save("unique123", 101.5);
        assertNotNull(savedReading);
        assertEquals("unique123", savedReading.getGaugeUniqueIdentificator());
        assertEquals(101.5, savedReading.getPressure());
        assertEquals("system123", savedReading.getSystemId());
        assertEquals(12.34, savedReading.getMeasureLat());
        assertEquals(56.78, savedReading.getMeasureLong());

        verify(listOperations, times(1)).leftPush("pressureReadingsProcessQueue", "unique123");
        verify(listOperations, times(1)).leftPush("GaugeMeasure_unique123", 101.5);
        verify(listOperations, times(1)).trim("GaugeMeasure_unique123", 0, 9);
    }

    @Test
    public void testSavePressureGaugeNotFound() {
        when(pressureGaugeRepository.findByGaugeUniqueIdentificator("unique123")).thenReturn(null);

        Exception exception = assertThrows(PressureGaugeNotFoundException.class, () -> {
            pressureGaugeReadingService.save("unique123", 101.5);
        });

        assertEquals("Pressure Gauge not found for gaugeUniqueIdentificator: unique123", exception.getMessage());
    }

    @Test
    public void testGetLastReadings() {
        String gaugeUniqueIdentificator = "unique123";
        String key = "GaugeMeasure_" + gaugeUniqueIdentificator;
        when(listOperations.range(key, 0, -1)).thenReturn(Arrays.asList(101.5, 102.5, 103.5));

        List<Double> readings = pressureGaugeReadingService.getLastReadings(gaugeUniqueIdentificator);
        assertNotNull(readings);
        assertEquals(3, readings.size());
        assertEquals(101.5, readings.get(0));
        assertEquals(102.5, readings.get(1));
        assertEquals(103.5, readings.get(2));
    }
}