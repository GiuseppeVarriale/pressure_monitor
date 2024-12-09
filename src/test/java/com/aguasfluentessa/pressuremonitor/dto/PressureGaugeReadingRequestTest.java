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

public class PressureGaugeReadingRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidPressureGaugeReadingRequest() {
        PressureGaugeReadingRequest request = new PressureGaugeReadingRequest();
        request.setGaugeUniqueIdentificator("unique123");
        request.setPressure(101.5);

        Set<ConstraintViolation<PressureGaugeReadingRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testGaugeUniqueIdentificatorCannotBeBlank() {
        PressureGaugeReadingRequest request = new PressureGaugeReadingRequest();
        request.setGaugeUniqueIdentificator("");
        request.setPressure(101.5);

        Set<ConstraintViolation<PressureGaugeReadingRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Gauge Unique Identificator cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void testPressureCannotBeNull() {
        PressureGaugeReadingRequest request = new PressureGaugeReadingRequest();
        request.setGaugeUniqueIdentificator("unique123");
        request.setPressure(null);

        Set<ConstraintViolation<PressureGaugeReadingRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Pressure cannot be blank or null", violations.iterator().next().getMessage());
    }
}