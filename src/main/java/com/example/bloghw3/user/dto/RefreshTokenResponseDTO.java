package com.example.bloghw3.user.dto;

import lombok.Getter;

@Getter
public class RefreshTokenResponseDTO {

    private String msg;

    private int status;

    private String accessToken;

    public RefreshTokenResponseDTO(String msg, int status, String accessToken) {
        this.msg = msg;
        this.status = status;
        this.accessToken = accessToken;
    }
}
