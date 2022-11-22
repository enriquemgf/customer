package com.bradesco.exception;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Since I'm using {@link javax.validation.Valid} and its validations annotations on my DTO classes,
 * I need to catch and handle {@link MethodArgumentNotValidException} that are thrown
 * and here I mimic spring-boot's error format with timestamp, path, status, etc.
 * @author enrique.guijarro
 * @since Nov-2022
 */
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    // example: 2021-03-24T16:44:39.083+08:00
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String uri = request.getDescription(false).split("=")[1];
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("timestamp", sdf.format(new Date()));
        errorMap.put("status", HttpStatus.BAD_REQUEST.value());
        errorMap.put("path", uri);

        List<ErrorDef> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            ErrorDef errorDef = new ErrorDef();
            errorDef.setError(error.getDefaultMessage());
            errorDef.setField(error.getField());
            errorDef.setRejectedValue(error.getRejectedValue());
            errors.add(errorDef);
        }
        errorMap.put("errors", errors);

        return handleExceptionInternal(ex, errorMap, headers, HttpStatus.BAD_REQUEST, request);
    }

}
