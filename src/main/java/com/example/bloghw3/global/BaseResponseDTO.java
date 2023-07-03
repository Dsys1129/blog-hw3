package com.example.bloghw3.global;

import lombok.Getter;

@Getter
public class BaseResponseDTO {

    private final int status;

    private final String message;

    public BaseResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
