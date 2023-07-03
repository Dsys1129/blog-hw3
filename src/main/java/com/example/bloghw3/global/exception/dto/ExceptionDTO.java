package com.example.bloghw3.global.exception.dto;

import java.util.Map;

import lombok.Getter;

@Getter
public class ExceptionDTO {
    private final String success;
    private final int status;
    private final Map<String, String> errors;

    public ExceptionDTO(String success, int status, Map<String, String> errors) {
        this.success = success;
        this.status = status;
        this.errors = errors;
    }
}
