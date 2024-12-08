package com.aguasfluentessa.pressuremonitor.controller.api.v1;

import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;
import com.aguasfluentessa.pressuremonitor.model.Exception.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.service.PressureGaugeReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pressure-gauge-readings")
public class PressureGaugeReadingController extends AbstractV1ApiController {

    @Autowired
    private PressureGaugeReadingService pressureGaugeReadingService;

    @GetMapping
    public ResponseEntity<List<PressureGaugeReading>> listAllReadings(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Double minPressure,
            @RequestParam(required = false) Double maxPressure) {
        List<PressureGaugeReading> readings = pressureGaugeReadingService.findByFilters(startDate, endDate, minPressure,
                maxPressure);
        return ResponseEntity.ok(readings);
    }

    @PostMapping
    public ResponseEntity<PressureGaugeReading> createReading(@RequestParam String gaugeUniqueIdentificator,
            @RequestParam Double pressure) {
        PressureGaugeReading reading = pressureGaugeReadingService.save(gaugeUniqueIdentificator, pressure);
        return ResponseEntity.status(HttpStatus.CREATED).body(reading);
    }


    @ExceptionHandler(PressureGaugeNotFoundException.class)
    public ResponseEntity<String> handlePressureGaugeNotFoundException(PressureGaugeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}