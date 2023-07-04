package com.example.bloghw3.global;

import lombok.Getter;

@Getter
public class BaseResponseDTO {

    private final String msg;

    private final int status;

    public BaseResponseDTO(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }
}
