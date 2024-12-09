package com.aguasfluentessa.pressuremonitor.controller.api.v1;

import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;
import com.aguasfluentessa.pressuremonitor.model.Exception.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.service.PressureGaugeReadingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PressureGaugeReadingController.class)
public class PressureGaugeReadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PressureGaugeReadingService pressureGaugeReadingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAllReadings() throws Exception {
        PressureGaugeReading reading1 = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);
        PressureGaugeReading reading2 = new PressureGaugeReading("unique456", 102.5, "system456", 23.45, 67.89);
        List<PressureGaugeReading> readings = Arrays.asList(reading1, reading2);

        when(pressureGaugeReadingService.findByFilters(any(LocalDateTime.class), any(LocalDateTime.class), anyDouble(), anyDouble()))
                .thenReturn(readings);

        mockMvc.perform(get("/api/v1/pressure-gauge-readings")
                .param("startDate", "2023-01-01T00:00:00")
                .param("endDate", "2023-12-31T23:59:59")
                .param("minPressure", "100.0")
                .param("maxPressure", "105.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gaugeUniqueIdentificator").value("unique123"))
                .andExpect(jsonPath("$[0].pressure").value(101.5))
                .andExpect(jsonPath("$[1].gaugeUniqueIdentificator").value("unique456"))
                .andExpect(jsonPath("$[1].pressure").value(102.5));
    }

    @Test
    public void testListReadingsWithFilters() throws Exception {
        PressureGaugeReading reading1 = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);
        PressureGaugeReading reading2 = new PressureGaugeReading("unique456", 102.5, "system456", 23.45, 67.89);
        List<PressureGaugeReading> readings = Arrays.asList(reading1, reading2);

        when(pressureGaugeReadingService.findByFilters(any(LocalDateTime.class), any(LocalDateTime.class), anyDouble(), anyDouble()))
                .thenReturn(readings);

        mockMvc.perform(get("/api/v1/pressure-gauge-readings")
                .param("startDate", "2023-01-01T00:00:00")
                .param("endDate", "2023-12-31T23:59:59")
                .param("minPressure", "100.0")
                .param("maxPressure", "105.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gaugeUniqueIdentificator").value("unique123"))
                .andExpect(jsonPath("$[0].pressure").value(101.5))
                .andExpect(jsonPath("$[1].gaugeUniqueIdentificator").value("unique456"))
                .andExpect(jsonPath("$[1].pressure").value(102.5));
    }


    @Test
    public void testCreateReading() throws Exception {
        PressureGaugeReading reading = new PressureGaugeReading("unique123", 101.5, "system123", 12.34, 56.78);

        when(pressureGaugeReadingService.save(anyString(), anyDouble())).thenReturn(reading);

        mockMvc.perform(post("/api/v1/pressure-gauge-readings")
                .content("{\"gaugeUniqueIdentificator\":\"unique123\",\"pressure\":101.5}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gaugeUniqueIdentificator").value("unique123"))
                .andExpect(jsonPath("$.pressure").value(101.5));
    }

    @Test
    public void testCreateReadingWithBlankGaugeUniqueIdentificator() throws Exception {
        mockMvc.perform(post("/api/v1/pressure-gauge-readings")
                .content("{\"gaugeUniqueIdentificator\":\"\",\"pressure\":101.5}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.gaugeUniqueIdentificator").value("Gauge Unique Identificator cannot be blank"));
    }

    @Test
    public void testCreateReadingWithNullPressure() throws Exception {
        mockMvc.perform(post("/api/v1/pressure-gauge-readings")
                .content("{\"gaugeUniqueIdentificator\":\"unique123\",\"pressure\":null}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pressure").value("Pressure cannot be blank or null"));
    }

    @Test
    public void testCreateReadingPressureGaugeNotFound() throws Exception {
        when(pressureGaugeReadingService.save(anyString(), anyDouble())).thenThrow(new PressureGaugeNotFoundException("Pressure Gauge not found"));

        mockMvc.perform(post("/api/v1/pressure-gauge-readings")
                .content("{\"gaugeUniqueIdentificator\":\"unique123\",\"pressure\":101.5}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pressure Gauge not found"));
    }
}