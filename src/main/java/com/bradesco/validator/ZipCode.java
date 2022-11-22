package com.bradesco.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Constraint(validatedBy = {ZipCodeValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZipCode {

    String message() default "Invalid zip code. Please use format XXXXX-XXX.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
