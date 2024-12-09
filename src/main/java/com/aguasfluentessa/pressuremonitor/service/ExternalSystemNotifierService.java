package com.aguasfluentessa.pressuremonitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aguasfluentessa.pressuremonitor.Exceptions.ErrorSendindNotificationToMainServerException;
import com.aguasfluentessa.pressuremonitor.model.PressureNotification;

@Service
public class ExternalSystemNotifierService {
    private static final Logger logger = LoggerFactory.getLogger(ExternalSystemNotifierService.class);

    @Value("${MAIN_SYSTEM_URL}") String mainSystemUrl;

    @Value("${MAIN_SYSTEM_DOORKEEPER_APPLICATION_TOKEN}") String mainSystemDoorkeeperApplicationToken;

    private final RestTemplate restTemplate;

    public ExternalSystemNotifierService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void notifyExternalSystem(PressureNotification notification) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + mainSystemDoorkeeperApplicationToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<PressureNotification> request = new HttpEntity<>(notification, headers);

        int attempts = 0;
        while (attempts < 5) {
            try {
                ResponseEntity<String> response = restTemplate.exchange(mainSystemUrl, HttpMethod.POST, request,
                        String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    return;
                }
            } catch (Exception e) {
                logger.error("Failed to notify external system", e);
            }
            attempts++;
        }
    }
}
