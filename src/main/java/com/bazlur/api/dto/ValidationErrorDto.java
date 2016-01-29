package com.bazlur.api.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
public class ValidationErrorDto {
    private List<FieldErrorDto> fieldErrors = new ArrayList<>();

    public ValidationErrorDto() {
    }

    public void addFieldError(String path, String message) {
        FieldErrorDto fieldError = new FieldErrorDto(path, message);
        fieldErrors.add(fieldError);
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }
}
