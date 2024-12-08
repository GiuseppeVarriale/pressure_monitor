package com.aguasfluentessa.pressuremonitor.repository;

import com.aguasfluentessa.pressuremonitor.config.BaseTestConfig;
import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PressureGaugeRepositoryTest extends BaseTestConfig {

    @Autowired
    private PressureGaugeRepository pressureGaugeRepository;

    private PressureGauge gauge1;
    private PressureGauge gauge2;

    @BeforeEach
    public void setUp() {
        pressureGaugeRepository.deleteAll(); 
        
        gauge1 = new PressureGauge("system123", "unique123", 12.34, 56.78, true);
        gauge2 = new PressureGauge("system456", "unique456", 23.45, 67.89, false);
        pressureGaugeRepository.save(gauge1);
        pressureGaugeRepository.save(gauge2);
    }

    @Test
    public void testFindByActive() {
        List<PressureGauge> activeGauges = pressureGaugeRepository.findByActive(true);
        assertThat(activeGauges).hasSize(1).contains(gauge1);

        List<PressureGauge> inactiveGauges = pressureGaugeRepository.findByActive(false);
        assertThat(inactiveGauges).hasSize(1).contains(gauge2);
    }

    @Test
    public void testFindBySystemId() {
        PressureGauge foundGauge = pressureGaugeRepository.findBySystemId("system123");
        assertThat(foundGauge).isEqualTo(gauge1);
    }

    @Test
    public void testFindByGaugeUniqueIdentificator() {
        PressureGauge foundGauge = pressureGaugeRepository.findByGaugeUniqueIdentificator("unique123");
        assertThat(foundGauge).isEqualTo(gauge1);
    }

    @Test
    public void testSaveAndUpdate() {
        PressureGauge newGauge = new PressureGauge("system789", "unique789", 34.56, 78.90, true);
        PressureGauge savedGauge = pressureGaugeRepository.save(newGauge);
        assertThat(savedGauge.getId()).isNotNull();

        savedGauge.setActive(false);
        PressureGauge updatedGauge = pressureGaugeRepository.save(savedGauge);
        assertThat(updatedGauge.getActive()).isFalse();
    }
}