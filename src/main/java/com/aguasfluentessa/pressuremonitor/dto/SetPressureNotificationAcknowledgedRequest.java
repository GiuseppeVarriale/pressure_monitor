package com.aguasfluentessa.pressuremonitor.dto;

import jakarta.validation.constraints.NotNull;

public class SetPressureNotificationAcknowledgedRequest {
    @NotNull(message = "Notification ID is required")
    private Long notificationId;

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

}
