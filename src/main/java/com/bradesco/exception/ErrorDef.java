package com.bradesco.exception;

import lombok.Data;

/**
 * Meant for holding errors caught on {@link ExceptionHandler#handleMethodArgumentNotValid}.
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Data
public  class ErrorDef {
    private String error;
    private String field;
    private Object rejectedValue;
}
