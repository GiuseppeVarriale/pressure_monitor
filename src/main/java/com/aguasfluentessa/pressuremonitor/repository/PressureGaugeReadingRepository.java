package com.aguasfluentessa.pressuremonitor.repository;

import com.aguasfluentessa.pressuremonitor.model.PressureGaugeReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PressureGaugeReadingRepository extends JpaRepository<PressureGaugeReading, Long> {

        @Query("SELECT r FROM PressureGaugeReading r WHERE " +
                        "(COALESCE(:startDate, r.createdAt) = r.createdAt OR r.createdAt >= :startDate) AND " +
                        "(COALESCE(:endDate, r.createdAt) = r.createdAt OR r.createdAt <= :endDate) AND " +
                        "(COALESCE(:minPressure, r.pressure) = r.pressure OR r.pressure >= :minPressure) AND " +
                        "(COALESCE(:maxPressure, r.pressure) = r.pressure OR r.pressure <= :maxPressure)")
        List<PressureGaugeReading> findByFilters(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        @Param("minPressure") Double minPressure,
                        @Param("maxPressure") Double maxPressure);
}