package com.aguasfluentessa.pressuremonitor.model;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class PressureGaugeReadingTest {

    @Test
    public void testPressureGaugeReadingConstructor() {
        PressureGaugeReading reading = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);

        assertEquals("unique123", reading.getGaugeUniqueIdentificator());
        assertEquals(101.5, reading.getPressure());
        assertEquals("system123", reading.getSystemId());
        assertEquals(12.34, reading.getMeasureLat());
        assertEquals(56.78, reading.getMeasureLong());
    }

    @Test
    public void testPressureGaugeReadingFullConstructor() {
        LocalDateTime now = LocalDateTime.now();
        PressureGaugeReading reading = new PressureGaugeReading(1L, "unique123", 101.5, "system123", 12.34, 56.78, now);

        assertEquals(1L, reading.getId());
        assertEquals("unique123", reading.getGaugeUniqueIdentificator());
        assertEquals(101.5, reading.getPressure());
        assertEquals("system123", reading.getSystemId());
        assertEquals(12.34, reading.getMeasureLat());
        assertEquals(56.78, reading.getMeasureLong());
        assertEquals(now, reading.getCreatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        PressureGaugeReading reading = new PressureGaugeReading();
        reading.setGaugeUniqueIdentificator("unique123");
        reading.setPressure(101.5);
        reading.setSystemId("system123");
        reading.setMeasureLat(12.34);
        reading.setMeasureLong(56.78);

        assertEquals("unique123", reading.getGaugeUniqueIdentificator());
        assertEquals(101.5, reading.getPressure());
        assertEquals("system123", reading.getSystemId());
        assertEquals(12.34, reading.getMeasureLat());
        assertEquals(56.78, reading.getMeasureLong());
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        PressureGaugeReading reading1 = new PressureGaugeReading(1L, "unique123", 101.5, "system123", 12.34, 56.78, now);
        PressureGaugeReading reading2 = new PressureGaugeReading(1L, "unique123", 101.5, "system123", 12.34, 56.78, now);
        PressureGaugeReading reading3 = new PressureGaugeReading(2L, "unique456", 102.5, "system456", 23.45, 67.89, now);

        assertEquals(reading1, reading2);
        assertNotEquals(reading1, reading3);
        assertEquals(reading1.hashCode(), reading2.hashCode());
        assertNotEquals(reading1.hashCode(), reading3.hashCode());
    }

    @Test
    public void testToString() {
        PressureGaugeReading reading = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);
        String expected = "PressureGaugeReading{id=null, gaugeUniqueIdentificator='unique123', pressure=101.5, systemId='system123', measureLat=12.34, measureLong=56.78, createdAt=null}";
        assertEquals(expected, reading.toString());
    }    
}
