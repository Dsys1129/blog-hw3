package com.example.bloghw3.user.dto;

import lombok.Getter;

@Getter
public class UserResponseDTO {

    private final String success;
    private final int status;

    public UserResponseDTO(String success, int status) {
        this.success = success;
        this.status = status;
    }
}
