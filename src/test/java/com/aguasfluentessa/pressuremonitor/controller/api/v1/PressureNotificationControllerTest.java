package com.aguasfluentessa.pressuremonitor.controller.api.v1;

import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;
import com.aguasfluentessa.pressuremonitor.service.PressureNotificationService;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PressureNotificationController.class)
public class PressureNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PressureNotificationService pressureNotificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAllNotifications() throws Exception {
        PressureNotification notification1 = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false,
                LocalDateTime.now()
        );

        PressureNotification notification2 = new PressureNotification(
                "unique456",
                30.0,
                AlertLevel.CRITICAL,
                AlertType.LEAK,
                true,
                LocalDateTime.now().minusDays(1)
        );

        List<PressureNotification> notifications = Arrays.asList(notification1, notification2);

        when(pressureNotificationService.findByFilters(any(), any(), any(), any(), any(), any()))
                .thenReturn(notifications);

        mockMvc.perform(get("/api/v1/pressure-notifications")
                .param("startDate", "2023-01-01T00:00:00")
                .param("endDate", "2023-12-31T23:59:59")
                .param("acknowledged", "false")
                .param("gaugeUniqueIdentificator", "unique123")
                .param("alertLevel", "HIGH")
                .param("alertType", "HIGH_PRESSURE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gaugeUniqueIdentificator").value("unique123"))
                .andExpect(jsonPath("$[0].pressure").value(25.0))
                .andExpect(jsonPath("$[1].gaugeUniqueIdentificator").value("unique456"))
                .andExpect(jsonPath("$[1].pressure").value(30.0));
    }

    @Test
    public void testSetAcknowledgedValidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/pressure-notifications/setAcknowledged")
                .content("{\"notificationId\":1,\"acknowledged\":true}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(pressureNotificationService, times(1)).setAcknowledged(1L);
    }

    @Test
    public void testSetAcknowledgedInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/pressure-notifications/setAcknowledged")
                .content("{\"notificationId\":null,\"acknowledged\":true}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.notificationId").value("Notification ID is required"));

        verify(pressureNotificationService, times(0)).setAcknowledged(anyLong());
    }

    @Test
    public void testSetAcknowledgedNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Notification not found")).when(pressureNotificationService).setAcknowledged(1L);

        mockMvc.perform(post("/api/v1/pressure-notifications/setAcknowledged")
                .content("{\"notificationId\":1,\"acknowledged\":true}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Notification not found"));

        verify(pressureNotificationService, times(1)).setAcknowledged(1L);
    }
}