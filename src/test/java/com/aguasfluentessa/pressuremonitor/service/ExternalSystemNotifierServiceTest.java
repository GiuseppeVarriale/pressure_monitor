package com.aguasfluentessa.pressuremonitor.service;

import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

@ActiveProfiles("test")
public class ExternalSystemNotifierServiceTest{

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExternalSystemNotifierService externalSystemNotifierService;

 @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        externalSystemNotifierService = new ExternalSystemNotifierService(restTemplate);
        externalSystemNotifierService.mainSystemUrl = "http://example.com/api/notifications";
        externalSystemNotifierService.mainSystemDoorkeeperApplicationToken = "test_token";
    }

    @Test
    public void testNotifyExternalSystemSuccess() {
        PressureNotification notification = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false,
                LocalDateTime.now()
        );

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        externalSystemNotifierService.notifyExternalSystem(notification);

        ArgumentCaptor<HttpEntity> requestCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate, times(1)).exchange(anyString(), any(HttpMethod.class), requestCaptor.capture(), eq(String.class));

        HttpEntity<PressureNotification> request = requestCaptor.getValue();
        assertNotNull(request);
        assertEquals(notification, request.getBody());

        HttpHeaders headers = request.getHeaders();
        assertEquals("Bearer " + externalSystemNotifierService.mainSystemDoorkeeperApplicationToken, headers.getFirst(HttpHeaders.AUTHORIZATION));
        assertEquals(MediaType.APPLICATION_JSON_VALUE, headers.getFirst(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    public void testNotifyExternalSystemFailure() {
        PressureNotification notification = new PressureNotification(
                "unique123",
                25.0,
                AlertLevel.HIGH,
                AlertType.HIGH_PRESSURE,
                false,
                LocalDateTime.now()
        );

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Failed to notify external system"));

        externalSystemNotifierService.notifyExternalSystem(notification);

        verify(restTemplate, times(5)).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class));
    }
}