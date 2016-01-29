package com.bazlur.api.dto;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
public class FieldErrorDto {
    private String path;
    private String message;

    public FieldErrorDto(String path, String message) {
        this.path = path;
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }
}
