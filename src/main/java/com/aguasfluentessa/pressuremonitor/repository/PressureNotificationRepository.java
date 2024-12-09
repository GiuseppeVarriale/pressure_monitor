package com.aguasfluentessa.pressuremonitor.repository;

import com.aguasfluentessa.pressuremonitor.model.PressureNotification;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertLevel;
import com.aguasfluentessa.pressuremonitor.model.PressureNotificationEnums.AlertType;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PressureNotificationRepository extends JpaRepository<PressureNotification, Long> {
    @Query("SELECT r FROM PressureNotification r WHERE " +
            "(COALESCE(:startDate, r.referenceDateTime) = r.referenceDateTime OR r.referenceDateTime >= :startDate) AND " +
            "(COALESCE(:endDate, r.referenceDateTime) = r.referenceDateTime OR r.referenceDateTime <= :endDate) AND " +
            "(COALESCE(:acknowledged, r.acknowledged) = r.acknowledged OR r.acknowledged = :acknowledged) AND " +
            "(COALESCE(:gaugeUniqueIdentificator, r.gaugeUniqueIdentificator) = r.gaugeUniqueIdentificator OR r.gaugeUniqueIdentificator = :gaugeUniqueIdentificator) AND " +
            "(COALESCE(:alertLevel, r.alertLevel) = r.alertLevel OR r.alertLevel = :alertLevel) AND " +
            "(COALESCE(:alertType, r.alertType) = r.alertType OR r.alertType = :alertType)")
    List<PressureNotification> findByFilters(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate,
                                             @Param("acknowledged") Boolean acknowledged,
                                             @Param("gaugeUniqueIdentificator") String gaugeUniqueIdentificator,
                                             @Param("alertLevel") AlertLevel alertLevel,
                                             @Param("alertType") AlertType alertType);
}
