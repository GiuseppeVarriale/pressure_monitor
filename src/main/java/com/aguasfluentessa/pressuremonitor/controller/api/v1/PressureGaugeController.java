package com.aguasfluentessa.pressuremonitor.controller.api.v1;

import com.aguasfluentessa.pressuremonitor.Exceptions.DuplicatePressureGaugeException;
import com.aguasfluentessa.pressuremonitor.Exceptions.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.dto.PressureGaugeCreateRequest;
import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import com.aguasfluentessa.pressuremonitor.service.PressureGaugeService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pressure-gauges")
@Validated
public class PressureGaugeController extends AbstractV1ApiController {

    @Autowired
    private PressureGaugeService pressureGaugeService;

    @PostMapping
    public ResponseEntity<PressureGauge> createPressureGauge(@Valid @RequestBody PressureGaugeCreateRequest request) {
        PressureGauge pressureGauge = new PressureGauge(
            request.getSystemId(),
            request.getGaugeUniqueIdentificator(),
            request.getLat(),
            request.getLon(),
            request.getActive()
        );
        PressureGauge createdGauge = pressureGaugeService.save(pressureGauge);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGauge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PressureGauge> updatePressureGauge(@PathVariable Long id, @RequestBody PressureGauge pressureGauge) {
        PressureGauge updatedGauge = pressureGaugeService.update(id, pressureGauge);
        return ResponseEntity.ok(updatedGauge);
    }

    @GetMapping
    public ResponseEntity<List<PressureGauge>> listPressureGaugesByActive(@RequestParam Boolean active) {
        List<PressureGauge> gauges = pressureGaugeService.findByStatus(active);
        return ResponseEntity.ok(gauges);
    }

    @GetMapping("/system-id/{systemId}")
    public ResponseEntity<PressureGauge> showPressureGaugeBySystemId(@PathVariable String systemId) {
        PressureGauge gauge = pressureGaugeService.findBySystemId(systemId);
        return ResponseEntity.ok(gauge);
    }

    @GetMapping("/gauge-unique-id/{gaugeUniqueIdentificator}")
    public ResponseEntity<PressureGauge> showPressureGaugeByGaugeUniqueIdentificator(@PathVariable String gaugeUniqueIdentificator) {
        PressureGauge gauge = pressureGaugeService.findByGaugeUniqueIdentificator(gaugeUniqueIdentificator);
        return ResponseEntity.ok(gauge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePressureGaugeById(@PathVariable Long id) {
        pressureGaugeService.disable_by_id(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/system-id/{systemId}")
    public ResponseEntity<Void> deletePressureGaugeBySystemId(@PathVariable String systemId) {
        pressureGaugeService.disable_by_system_id(systemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/gauge-unique-id/{gaugeUniqueIdentificator}")
    public ResponseEntity<Void> deletePressureGaugeByGaugeUniqueIdentificator(@PathVariable String gaugeUniqueIdentificator) {
        pressureGaugeService.disable_by_gauge_unique_identificator(gaugeUniqueIdentificator);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(PressureGaugeNotFoundException.class)
    public ResponseEntity<String> handlePressureGaugeNotFoundException(PressureGaugeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicatePressureGaugeException.class)
    public ResponseEntity<String> handlePressureGaugeNotFoundException(DuplicatePressureGaugeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}