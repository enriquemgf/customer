package com.bradesco.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Test;

class ZipCodeValidatorTest {

    @Test
    void testIsValid() {
        ZipCodeValidator validator = new ZipCodeValidator();
        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);
        assertFalse(validator.isValid("", mockContext));
        assertFalse(validator.isValid("0001a-000", mockContext));
        assertFalse(validator.isValid("a00010-000", mockContext));
        assertFalse(validator.isValid("a00010-00a", mockContext));
        assertTrue(validator.isValid("99999-999", mockContext));
    }
}