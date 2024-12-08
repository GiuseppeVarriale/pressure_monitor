package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import com.aguasfluentessa.pressuremonitor.model.Exception.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PressureGaugeServiceTest {

    @Mock
    private PressureGaugeRepository pressureGaugeRepository;

    @InjectMocks
    private PressureGaugeService pressureGaugeService;

    private PressureGauge gauge1;
    private PressureGauge gauge2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gauge1 = new PressureGauge(1L, "system123", "unique123", 12.34, 56.78, true, null, null);
        gauge2 = new PressureGauge(2L, "system456", "unique456", 23.45, 67.89, false, null, null);
    }

    @Test
    public void testFindAll() {
        when(pressureGaugeRepository.findAll()).thenReturn(Arrays.asList(gauge1, gauge2));
        List<PressureGauge> gauges = pressureGaugeService.findAll();
        assertEquals(2, gauges.size());
        verify(pressureGaugeRepository, times(1)).findAll();
    }

    @Test
    public void testFindBySystemId() {
        when(pressureGaugeRepository.findBySystemId("system123")).thenReturn(gauge1);
        PressureGauge foundGauge = pressureGaugeService.findBySystemId("system123");
        assertEquals(gauge1, foundGauge);
        verify(pressureGaugeRepository, times(1)).findBySystemId("system123");
    }

    @Test
    public void testFindBySystemIdNotFound() {
        when(pressureGaugeRepository.findBySystemId("system789")).thenReturn(null);
        assertThrows(PressureGaugeNotFoundException.class, () -> {
            pressureGaugeService.findBySystemId("system789");
        });
        verify(pressureGaugeRepository, times(1)).findBySystemId("system789");
    }

    @Test
    public void testFindByGaugeUniqueIdentificator() {
        when(pressureGaugeRepository.findByGaugeUniqueIdentificator("unique123")).thenReturn(gauge1);
        PressureGauge foundGauge = pressureGaugeService.findByGaugeUniqueIdentificator("unique123");
        assertEquals(gauge1, foundGauge);
        verify(pressureGaugeRepository, times(1)).findByGaugeUniqueIdentificator("unique123");
    }

    @Test
    public void testFindByGaugeUniqueIdentificatorNotFound() {
        when(pressureGaugeRepository.findByGaugeUniqueIdentificator("unique789")).thenReturn(null);
        assertThrows(PressureGaugeNotFoundException.class, () -> {
            pressureGaugeService.findByGaugeUniqueIdentificator("unique789");
        });
        verify(pressureGaugeRepository, times(1)).findByGaugeUniqueIdentificator("unique789");
    }

    @Test
    public void testSave() {
        when(pressureGaugeRepository.save(gauge1)).thenReturn(gauge1);
        PressureGauge savedGauge = pressureGaugeService.save(gauge1);
        assertEquals(gauge1, savedGauge);
        verify(pressureGaugeRepository, times(1)).save(gauge1);
    }

    @Test
    public void testDisableById() {
        when(pressureGaugeRepository.findById(1L)).thenReturn(Optional.of(gauge1));
        pressureGaugeService.disable_by_id(1L);
        assertFalse(gauge1.getActive());
        verify(pressureGaugeRepository, times(1)).findById(1L);
        verify(pressureGaugeRepository, times(1)).save(gauge1);
    }

    @Test
    public void testDisableByIdNotFound() {
        when(pressureGaugeRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(PressureGaugeNotFoundException.class, () -> {
            pressureGaugeService.disable_by_id(3L);
        });
        verify(pressureGaugeRepository, times(1)).findById(3L);
    }

    @Test
    public void testDisableBySystemId() {
        when(pressureGaugeRepository.findBySystemId("system123")).thenReturn(gauge1);
        pressureGaugeService.disable_by_system_id("system123");
        assertFalse(gauge1.getActive());
        verify(pressureGaugeRepository, times(1)).findBySystemId("system123");
        verify(pressureGaugeRepository, times(1)).save(gauge1);
    }

    @Test
    public void testDisableBySystemIdNotFound() {
        when(pressureGaugeRepository.findBySystemId("system789")).thenReturn(null);
        assertThrows(PressureGaugeNotFoundException.class, () -> {
            pressureGaugeService.disable_by_system_id("system789");
        });
        verify(pressureGaugeRepository, times(1)).findBySystemId("system789");
    }

    @Test
    public void testDisableByGaugeUniqueIdentificator() {
        when(pressureGaugeRepository.findByGaugeUniqueIdentificator("unique123")).thenReturn(gauge1);
        pressureGaugeService.disable_by_gauge_unique_identificator("unique123");
        assertFalse(gauge1.getActive());
        verify(pressureGaugeRepository, times(1)).findByGaugeUniqueIdentificator("unique123");
        verify(pressureGaugeRepository, times(1)).save(gauge1);
    }

    @Test
    public void testDisableByGaugeUniqueIdentificatorNotFound() {
        when(pressureGaugeRepository.findByGaugeUniqueIdentificator("unique789")).thenReturn(null);
        assertThrows(PressureGaugeNotFoundException.class, () -> {
            pressureGaugeService.disable_by_gauge_unique_identificator("unique789");
        });
        verify(pressureGaugeRepository, times(1)).findByGaugeUniqueIdentificator("unique789");
    }
}