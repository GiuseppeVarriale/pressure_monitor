package com.aguasfluentessa.pressuremonitor.repository;

import com.aguasfluentessa.pressuremonitor.config.BaseTestConfig;
import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;

import jakarta.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PressureGaugeReadingRepositoryTest extends BaseTestConfig {

    @Autowired
    private PressureGaugeReadingRepository pressureGaugeReadingRepository;

    @Autowired
    private PressureGaugeRepository pressureGaugeRepository;

    @Autowired
    private SessionFactory sessionFactory;

    private PressureGauge gauge1;
    private PressureGauge gauge2;
    private PressureGaugeReading reading1;
    private PressureGaugeReading reading2;
    private PressureGaugeReading reading3;

    @BeforeEach
    public void setUp() {
        pressureGaugeReadingRepository.deleteAll();
        pressureGaugeRepository.deleteAll();

        gauge1 = new PressureGauge("system123", "unique123", 12.34, 56.78, true);
        gauge2 = new PressureGauge("system456", "unique456", 23.45, 67.89, false);
        pressureGaugeRepository.save(gauge1);
        pressureGaugeRepository.save(gauge2);

        reading1 = new PressureGaugeReading(gauge1.getGaugeUniqueIdentificator(), 101.5, "system123", 12.34, 56.78);
        reading2 = new PressureGaugeReading(gauge1.getGaugeUniqueIdentificator(), 102.5, "system123", 12.34, 56.78);
        reading3 = new PressureGaugeReading(gauge2.getGaugeUniqueIdentificator(), 103.5, "system456", 23.45, 67.89);

        pressureGaugeReadingRepository.save(reading1);
        pressureGaugeReadingRepository.save(reading2);
        pressureGaugeReadingRepository.save(reading3);

    }

    @Test
    public void testFindByFilters_NoFilters() {
        List<PressureGaugeReading> readings = pressureGaugeReadingRepository.findByFilters(null, null, null, null);
        assertThat(readings).hasSize(3).contains(reading1, reading2, reading3);
    }

    @Test
    public void testFindByFilters_MinPressure() {
        List<PressureGaugeReading> readings = pressureGaugeReadingRepository.findByFilters(null, null, 102.0, null);
        assertThat(readings).hasSize(2).contains(reading2, reading3);
    }

    @Test
    public void testFindByFilters_MaxPressure() {
        List<PressureGaugeReading> readings = pressureGaugeReadingRepository.findByFilters(null, null, null, 102.5);
        assertThat(readings).hasSize(2).contains(reading1, reading2);
    }

}