package com.example.bloghw3.user.dto;

import lombok.Getter;

@Getter
public class LoginResponseDTO {

    private String success;

    private int status;

    private String accessToken;

    private String refreshToken;


    public LoginResponseDTO(String success, int status, String accessToken, String refreshToken) {
        this.success = success;
        this.status = status;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
