package com.bradesco.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author enrique.guijarro
 * @since Nov-2022
 */
public class ZipCodeValidator
    implements ConstraintValidator<ZipCode, String> {

    public static final String ZIPCODE_REGEX = "^[0-9]{5}(?:-[0-9]{3})?$";

    @Override
    public void initialize(final ZipCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return value.matches(ZIPCODE_REGEX);
    }
}
