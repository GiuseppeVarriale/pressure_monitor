package com.aguasfluentessa.pressuremonitor.controller.api.v1;

import com.aguasfluentessa.pressuremonitor.dto.SetPressureNotificationAcknowledgedRequest;
import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums;
import com.aguasfluentessa.pressuremonitor.service.PressureNotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pressure-notifications")
@Validated
public class PressureNotificationController extends AbstractV1ApiController {

    @Autowired
    private PressureNotificationService pressureNotificationService;

    @GetMapping
    public ResponseEntity<List<PressureNotification>> listAllNotifications(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Boolean acknowledged,
            @RequestParam(required = false) String gaugeUniqueIdentificator,
            @RequestParam(required = false) PressureNotificationEnums.AlertLevel alertLevel,
            @RequestParam(required = false) PressureNotificationEnums.AlertType alertType) {
        List<PressureNotification> notifications = pressureNotificationService.findByFilters(startDate, endDate, acknowledged,
                gaugeUniqueIdentificator, alertLevel, alertType);
        return ResponseEntity.ok(notifications);
    }


    
    @PostMapping("/setAcknowledged")
    public ResponseEntity<Void> setAcknowledged(@Valid @RequestBody SetPressureNotificationAcknowledgedRequest request) {
        pressureNotificationService.setAcknowledged(request.getNotificationId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}