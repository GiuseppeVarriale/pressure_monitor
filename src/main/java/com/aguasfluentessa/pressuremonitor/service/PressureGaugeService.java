package com.aguasfluentessa.pressuremonitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.aguasfluentessa.pressuremonitor.Exceptions.DuplicatePressureGaugeException;
import com.aguasfluentessa.pressuremonitor.Exceptions.PressureGaugeNotFoundException;
import com.aguasfluentessa.pressuremonitor.model.PressureGauge;
import com.aguasfluentessa.pressuremonitor.repository.PressureGaugeRepository;

@Service
public class PressureGaugeService {

    @Autowired
    private PressureGaugeRepository pressureGaugeRepository;

    public List<PressureGauge> findAll() {
        return pressureGaugeRepository.findAll();
    }

    public PressureGauge findBySystemId(String systemId) {
        PressureGauge gauge = pressureGaugeRepository.findBySystemId(systemId);
        if (gauge == null) {
            throw new PressureGaugeNotFoundException("Pressure Gauge not found");
        }
        return gauge;
    }

    public PressureGauge findByGaugeUniqueIdentificator(String gaugeUniqueIdentificator) {
        PressureGauge gauge = pressureGaugeRepository.findByGaugeUniqueIdentificator(gaugeUniqueIdentificator);
        if (gauge == null) {
            throw new PressureGaugeNotFoundException("Pressure Gauge not found");
        }
        return gauge;
    }

    public PressureGauge save(PressureGauge pressureGauge) {
        try {
            return pressureGaugeRepository.save(pressureGauge);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicatePressureGaugeException("Pressure Gauge with the same systemId or gaugeUniqueIdentificator already exists");
        }
    }
    

    public List<PressureGauge> findByStatus(Boolean active) {
        return pressureGaugeRepository.findByActive(active);
    }

    public PressureGauge update(Long id, PressureGauge pressureGauge) {
        PressureGauge existingGauge = pressureGaugeRepository.findById(id)
                .orElseThrow(() -> new PressureGaugeNotFoundException("Pressure Gauge not found"));

        if (pressureGauge.getSystemId() != null) {
            existingGauge.setSystemId(pressureGauge.getSystemId());
        }
        if (pressureGauge.getGaugeUniqueIdentificator() != null) {
            existingGauge.setGaugeUniqueIdentificator(pressureGauge.getGaugeUniqueIdentificator());
        }
        if (pressureGauge.getLat() != null) {
            existingGauge.setLat(pressureGauge.getLat());
        }
        if (pressureGauge.getLon() != null) {
            existingGauge.setLon(pressureGauge.getLon());
        }
        if (pressureGauge.getActive() != null) {
            existingGauge.setActive(pressureGauge.getActive());
        }

        try {
            return pressureGaugeRepository.save(existingGauge);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicatePressureGaugeException("Pressure Gauge with the same systemId or gaugeUniqueIdentificator already exists");
        }
    }

    public void disable_by_id(Long id) {
        PressureGauge gauge = pressureGaugeRepository.findById(id)
                .orElseThrow(() -> new PressureGaugeNotFoundException("Pressure Gauge not found"));
        gauge.setActive(false);
        pressureGaugeRepository.save(gauge);
    }

    public void disable_by_system_id(String systemId) {
        PressureGauge gauge = pressureGaugeRepository.findBySystemId(systemId);
        if (gauge == null) {
            throw new PressureGaugeNotFoundException("Pressure Gauge not found");
        }
        gauge.setActive(false);
        pressureGaugeRepository.save(gauge);
    }

    public void disable_by_gauge_unique_identificator(String gaugeUniqueIdentificator) {
        PressureGauge gauge = pressureGaugeRepository.findByGaugeUniqueIdentificator(gaugeUniqueIdentificator);
        if (gauge == null) {
            throw new PressureGaugeNotFoundException("Pressure Gauge not found");
        }
        gauge.setActive(false);
        pressureGaugeRepository.save(gauge);
    }
}
