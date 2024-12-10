package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.exceptions.DuplicatePressureGaugeException;
import com.aguasfluentessa.pressuremonitor.exceptions.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    public void testFindByStatus() {
        when(pressureGaugeRepository.findByActive(true)).thenReturn(Arrays.asList(gauge1));
        when(pressureGaugeRepository.findByActive(false)).thenReturn(Arrays.asList(gauge2));

        List<PressureGauge> activeGauges = pressureGaugeService.findByStatus(true);
        assertEquals(1, activeGauges.size());
        assertTrue(activeGauges.contains(gauge1));

        List<PressureGauge> inactiveGauges = pressureGaugeService.findByStatus(false);
        assertEquals(1, inactiveGauges.size());
        assertTrue(inactiveGauges.contains(gauge2));

        verify(pressureGaugeRepository, times(1)).findByActive(true);
        verify(pressureGaugeRepository, times(1)).findByActive(false);
    }

    @Test
    public void testSave() {
        when(pressureGaugeRepository.save(gauge1)).thenReturn(gauge1);
        PressureGauge savedGauge = pressureGaugeService.save(gauge1);
        assertEquals(gauge1, savedGauge);
        verify(pressureGaugeRepository, times(1)).save(gauge1);
    }

    @Test
    public void testSaveDuplicate() {
        when(pressureGaugeRepository.save(any(PressureGauge.class))).thenThrow(DataIntegrityViolationException.class);
        assertThrows(DuplicatePressureGaugeException.class, () -> {
            pressureGaugeService.save(gauge1);
        });
        verify(pressureGaugeRepository, times(1)).save(gauge1);
    }

    @Test
    public void testUpdate() {
        PressureGauge updatedGaugeData = new PressureGauge();
        updatedGaugeData.setSystemId("updatedSystem");
        updatedGaugeData.setGaugeUniqueIdentificator("updatedUnique");
        updatedGaugeData.setLat(45.67);
        updatedGaugeData.setLon(89.01);
        updatedGaugeData.setActive(false);

        when(pressureGaugeRepository.findById(1L)).thenReturn(Optional.of(gauge1));
        when(pressureGaugeRepository.save(any(PressureGauge.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PressureGauge updatedGauge = pressureGaugeService.update(1L, updatedGaugeData);

        assertEquals("updatedSystem", updatedGauge.getSystemId());
        assertEquals("updatedUnique", updatedGauge.getGaugeUniqueIdentificator());
        assertEquals(45.67, updatedGauge.getLat());
        assertEquals(89.01, updatedGauge.getLon());
        assertFalse(updatedGauge.getActive());

        verify(pressureGaugeRepository, times(1)).findById(1L);
        verify(pressureGaugeRepository, times(1)).save(updatedGauge);
    }

    @Test
    public void testUpdateDuplicate() {
        when(pressureGaugeRepository.findById(anyLong())).thenReturn(Optional.of(gauge1));
        when(pressureGaugeRepository.save(any(PressureGauge.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicatePressureGaugeException.class, () -> {
            pressureGaugeService.update(1L, gauge1);
        });
        verify(pressureGaugeRepository, times(1)).findById(anyLong());
        verify(pressureGaugeRepository, times(1)).save(gauge1);
    }

    @Test
    public void testUpdateNotFound() {
        PressureGauge updatedGaugeData = new PressureGauge();
        updatedGaugeData.setSystemId("updatedSystem");

        when(pressureGaugeRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(PressureGaugeNotFoundException.class, () -> {
            pressureGaugeService.update(3L, updatedGaugeData);
        });

        verify(pressureGaugeRepository, times(1)).findById(3L);
        verify(pressureGaugeRepository, times(0)).save(any(PressureGauge.class));
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