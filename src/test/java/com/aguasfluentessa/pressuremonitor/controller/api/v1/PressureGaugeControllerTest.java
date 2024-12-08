package com.aguasfluentessa.pressuremonitor.controller.api.v1;

import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import com.aguasfluentessa.pressuremonitor.model.Exception.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.service.PressureGaugeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PressureGaugeController.class)
public class PressureGaugeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PressureGaugeService pressureGaugeService;

    private PressureGauge gauge1;
    private PressureGauge gauge2;

    @BeforeEach
    public void setUp() {
        gauge1 = new PressureGauge(1L, "system123", "unique123", 12.34, 56.78, true, null, null);
        gauge2 = new PressureGauge(2L, "system456", "unique456", 23.45, 67.89, false, null, null);
    }

    @Test
    public void testCreatePressureGauge() throws Exception {
        when(pressureGaugeService.save(any(PressureGauge.class))).thenReturn(gauge1);

        mockMvc.perform(post("/pressure-gauges")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"systemId\":\"system123\",\"gaugeUniqueIdentificator\":\"unique123\",\"lat\":12.34,\"lon\":56.78,\"active\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.systemId").value("system123"))
                .andExpect(jsonPath("$.gaugeUniqueIdentificator").value("unique123"));

        verify(pressureGaugeService, times(1)).save(any(PressureGauge.class));
    }

    @Test
    public void testUpdatePressureGauge() throws Exception {
        PressureGauge updatedGauge = new PressureGauge(1L, "updatedSystem", "updatedUnique", 45.67, 89.01, false, null, null);
        when(pressureGaugeService.update(anyLong(), any(PressureGauge.class))).thenReturn(updatedGauge);

        mockMvc.perform(put("/pressure-gauges/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"systemId\":\"updatedSystem\",\"gaugeUniqueIdentificator\":\"updatedUnique\",\"lat\":45.67,\"lon\":89.01,\"active\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.systemId").value("updatedSystem"))
                .andExpect(jsonPath("$.gaugeUniqueIdentificator").value("updatedUnique"))
                .andExpect(jsonPath("$.lat").value(45.67))
                .andExpect(jsonPath("$.lon").value(89.01))
                .andExpect(jsonPath("$.active").value(false));

        verify(pressureGaugeService, times(1)).update(anyLong(), any(PressureGauge.class));
    }

    @Test
    public void testUpdatePressureGaugeNotFound() throws Exception {
        when(pressureGaugeService.update(anyLong(), any(PressureGauge.class))).thenThrow(new PressureGaugeNotFoundException("Pressure Gauge not found"));

        mockMvc.perform(put("/pressure-gauges/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"systemId\":\"updatedSystem\",\"gaugeUniqueIdentificator\":\"updatedUnique\",\"lat\":45.67,\"lon\":89.01,\"active\":false}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pressure Gauge not found"));

        verify(pressureGaugeService, times(1)).update(anyLong(), any(PressureGauge.class));
    }

    @Test
    public void testListPressureGaugesByActive() throws Exception {
        when(pressureGaugeService.findByStatus(true)).thenReturn(Arrays.asList(gauge1));
        when(pressureGaugeService.findByStatus(false)).thenReturn(Arrays.asList(gauge2));

        mockMvc.perform(get("/pressure-gauges?active=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].systemId").value("system123"));

        mockMvc.perform(get("/pressure-gauges?active=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].systemId").value("system456"));

        verify(pressureGaugeService, times(1)).findByStatus(true);
        verify(pressureGaugeService, times(1)).findByStatus(false);
    }

    @Test
    public void testShowPressureGaugeBySystemId() throws Exception {
        when(pressureGaugeService.findBySystemId(anyString())).thenReturn(gauge1);

        mockMvc.perform(get("/pressure-gauges/system-id/system123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.systemId").value("system123"));

        verify(pressureGaugeService, times(1)).findBySystemId(anyString());
    }
    
    @Test
    public void testShowPressureGaugeBySystemIdNotFound() throws Exception {
        when(pressureGaugeService.findBySystemId(anyString())).thenThrow(new PressureGaugeNotFoundException("Pressure Gauge not found"));

        mockMvc.perform(get("/pressure-gauges/system-id/system123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pressure Gauge not found"));

        verify(pressureGaugeService, times(1)).findBySystemId(anyString());
    }
    
    @Test
    public void testShowPressureGaugeByGaugeUniqueIdentificator() throws Exception {
        when(pressureGaugeService.findByGaugeUniqueIdentificator(anyString())).thenReturn(gauge1);

        mockMvc.perform(get("/pressure-gauges/gauge-unique-id/unique123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gaugeUniqueIdentificator").value("unique123"));

        verify(pressureGaugeService, times(1)).findByGaugeUniqueIdentificator(anyString());
    }

    @Test
    public void testShowPressureGaugeByGaugeUniqueIdentificatorNotFound() throws Exception {
        when(pressureGaugeService.findByGaugeUniqueIdentificator(anyString())).thenThrow(new PressureGaugeNotFoundException("Pressure Gauge not found"));

        mockMvc.perform(get("/pressure-gauges/gauge-unique-id/unique123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pressure Gauge not found"));

        verify(pressureGaugeService, times(1)).findByGaugeUniqueIdentificator(anyString());
    }

    @Test
    public void testDeletePressureGaugeById() throws Exception {
        doNothing().when(pressureGaugeService).disable_by_id(anyLong());

        mockMvc.perform(delete("/pressure-gauges/1"))
                .andExpect(status().isNoContent());

        verify(pressureGaugeService, times(1)).disable_by_id(anyLong());
    }

    @Test
    public void testDeletePressureGaugeByIdNotFound() throws Exception {
        doThrow(new PressureGaugeNotFoundException("Pressure Gauge not found")).when(pressureGaugeService).disable_by_id(anyLong());

        mockMvc.perform(delete("/pressure-gauges/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pressure Gauge not found"));

        verify(pressureGaugeService, times(1)).disable_by_id(anyLong());
    }

    @Test
    public void testDeletePressureGaugeBySystemIdNotFound() throws Exception {
        doThrow(new PressureGaugeNotFoundException("Pressure Gauge not found")).when(pressureGaugeService).disable_by_system_id(anyString());

        mockMvc.perform(delete("/pressure-gauges/system-id/system123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pressure Gauge not found"));

        verify(pressureGaugeService, times(1)).disable_by_system_id(anyString());
    }

    @Test
    public void testDeletePressureGaugeBySystemId() throws Exception {
        doNothing().when(pressureGaugeService).disable_by_system_id(anyString());

        mockMvc.perform(delete("/pressure-gauges/system-id/system123"))
                .andExpect(status().isNoContent());

        verify(pressureGaugeService, times(1)).disable_by_system_id(anyString());
    }

    @Test
    public void testDeletePressureGaugeByGaugeUniqueIdentificator() throws Exception {
        doNothing().when(pressureGaugeService).disable_by_gauge_unique_identificator(anyString());

        mockMvc.perform(delete("/pressure-gauges/gauge-unique-id/unique123"))
                .andExpect(status().isNoContent());

        verify(pressureGaugeService, times(1)).disable_by_gauge_unique_identificator(anyString());
    }

    @Test
    public void testDeletePressureGaugeByGaugeUniqueIdentificatorNotFound() throws Exception {
        doThrow(new PressureGaugeNotFoundException("Pressure Gauge not found")).when(pressureGaugeService).disable_by_gauge_unique_identificator(anyString());

        mockMvc.perform(delete("/pressure-gauges/gauge-unique-id/unique123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pressure Gauge not found"));

        verify(pressureGaugeService, times(1)).disable_by_gauge_unique_identificator(anyString());
    }

    @Test
    public void testHandleGenericException() throws Exception {
        when(pressureGaugeService.findBySystemId(anyString())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/pressure-gauges/system-id/system123"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred: Unexpected error"));

        verify(pressureGaugeService, times(1)).findBySystemId(anyString());
    }
}