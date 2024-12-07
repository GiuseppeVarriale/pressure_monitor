
package com.aguasfluentessa.pressuremonitor.model;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class PressureGaugeTest {

    @Test
    public void testPressureGaugeConstructor() {
        PressureGauge gauge = new PressureGauge("system123", "unique123", 12.34, 56.78, true);

        assertEquals("system123", gauge.getSystemId());
        assertEquals("unique123", gauge.getGaugeUniqueIdentificator());
        assertEquals(12.34, gauge.getLat());
        assertEquals(56.78, gauge.getLon());
        assertTrue(gauge.getActive());
    }

    @Test
    public void testPressureGaugeFullConstructor() {
        LocalDateTime now = LocalDateTime.now();
        PressureGauge gauge = new PressureGauge(1L, "system123", "unique123", 12.34, 56.78, true, now, now);

        assertEquals(1L, gauge.getId());
        assertEquals("system123", gauge.getSystemId());
        assertEquals("unique123", gauge.getGaugeUniqueIdentificator());
        assertEquals(12.34, gauge.getLat());
        assertEquals(56.78, gauge.getLon());
        assertTrue(gauge.getActive());
        assertEquals(now, gauge.getCreatedAt());
        assertEquals(now, gauge.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        PressureGauge gauge = new PressureGauge();
        gauge.setSystemId("system123");
        gauge.setGaugeUniqueIdentificator("unique123");
        gauge.setLat(12.34);
        gauge.setLon(56.78);
        gauge.setActive(true);

        assertEquals("system123", gauge.getSystemId());
        assertEquals("unique123", gauge.getGaugeUniqueIdentificator());
        assertEquals(12.34, gauge.getLat());
        assertEquals(56.78, gauge.getLon());
        assertTrue(gauge.getActive());
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        PressureGauge gauge1 = new PressureGauge(1L, "system123", "unique123", 12.34, 56.78, true, now, now);
        PressureGauge gauge2 = new PressureGauge(1L, "system123", "unique123", 12.34, 56.78, true, now, now);
        PressureGauge gauge3 = new PressureGauge(2L, "system456", "unique456", 23.45, 67.89, false, now, now);

        assertEquals(gauge1, gauge2);
        assertNotEquals(gauge1, gauge3);
        assertEquals(gauge1.hashCode(), gauge2.hashCode());
        assertNotEquals(gauge1.hashCode(), gauge3.hashCode());
    }

    @Test
    public void testToString() {
        PressureGauge gauge = new PressureGauge("system123", "unique123", 12.34, 56.78, true);
        String expected = "PressureGauge [id=null, systemId=system123, gaugeUniqueIdentificator=unique123, lat=12.34, lon=56.78, active=true, createdAt=null, updatedAt=null]";
        assertEquals(expected, gauge.toString());
    }
}