package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.Exceptions.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.config.BaseTestConfig;
import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeReadingRepository;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("test")
public class PressureGaugeReadingServiceTest extends BaseTestConfig {

    @Mock
    private PressureGaugeReadingRepository pressureGaugeReadingRepository;

    @Mock
    private PressureGaugeRepository pressureGaugeRepository;

    @InjectMocks
    private PressureGaugeReadingService pressureGaugeReadingService;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        connectionFactory.getConnection().serverCommands().flushDb();
    }

    @AfterEach
    public void tearDown() {
        connectionFactory.getConnection().serverCommands().flushDb();
    }

    @Test
    public void testRedisConnection() {
        assertNotNull(connectionFactory.getConnection());
        String pingResponse = connectionFactory.getConnection().ping();
        assertNotNull(pingResponse);
        assertTrue(pingResponse.equalsIgnoreCase("PONG"));
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

    // @Test
    // public void testSave() {
    //     PressureGauge pressureGauge = new PressureGauge("system123", "unique123", 12.34, 56.78, true);
    //     when(pressureGaugeRepository.findByGaugeUniqueIdentificator("unique123")).thenReturn(pressureGauge);

    //     PressureGaugeReading reading = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);
    //     when(pressureGaugeReadingRepository.save(any(PressureGaugeReading.class))).thenReturn(reading);

    //     PressureGaugeReading savedReading = pressureGaugeReadingService.save("unique123", 101.5);
    //     assertNotNull(savedReading);
    //     assertEquals("unique123", savedReading.getGaugeUniqueIdentificator());
    //     assertEquals(101.5, savedReading.getPressure());
    //     assertEquals("system123", savedReading.getSystemId());
    //     assertEquals(12.34, savedReading.getMeasureLat());
    //     assertEquals(56.78, savedReading.getMeasureLong());

    // }

    @Test
    public void testSavePressureGaugeNotFound() {
        when(pressureGaugeRepository.findByGaugeUniqueIdentificator("unique123")).thenReturn(null);

        Exception exception = assertThrows(PressureGaugeNotFoundException.class, () -> {
            pressureGaugeReadingService.save("unique123", 101.5);
        });

        String expectedMessage = "Pressure Gauge not found for gaugeUniqueIdentificator: unique123";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}