package com.example.bloghw3.user.dto;

import lombok.Getter;

@Getter
public class RefreshTokenResponseDTO {

    private String success;

    private int status;

    private String accessToken;

    public RefreshTokenResponseDTO(String success, int status, String accessToken) {
        this.success = success;
        this.status = status;
        this.accessToken = accessToken;
    }
}
