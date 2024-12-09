package com.aguasfluentessa.pressuremonitor.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetPressureNotificationAcknowledgedRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidRequest() {
        SetPressureNotificationAcknowledgedRequest request = new SetPressureNotificationAcknowledgedRequest();
        request.setNotificationId(1L);

        Set<ConstraintViolation<SetPressureNotificationAcknowledgedRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNotificationIdCannotBeNull() {
        SetPressureNotificationAcknowledgedRequest request = new SetPressureNotificationAcknowledgedRequest();
        request.setNotificationId(null);

        Set<ConstraintViolation<SetPressureNotificationAcknowledgedRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Notification ID is required", violations.iterator().next().getMessage());
    }
} 