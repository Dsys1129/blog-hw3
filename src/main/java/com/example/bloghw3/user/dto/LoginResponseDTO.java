package com.example.bloghw3.user.dto;

import lombok.Getter;

@Getter
public class LoginResponseDTO {

    private String msg;

    private int status;

    private String accessToken;

    private String refreshToken;


    public LoginResponseDTO(String msg, int status, String accessToken, String refreshToken) {
        this.msg = msg;
        this.status = status;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
