package com.aguasfluentessa.pressuremonitor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aguasfluentessa.pressuremonitor.model.PressureGauge;

public interface PressureGaugeRepository extends JpaRepository<PressureGauge, Long> {

    List<PressureGauge> findByActive(Boolean active);

    PressureGauge findBySystemId(String systemId);

    PressureGauge findByGaugeUniqueIdentificator(String gaugeUniqueIdentificator);
}
