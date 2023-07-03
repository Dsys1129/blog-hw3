package com.example.bloghw3.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequestDTO {

    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    private final String username;

    @Size(min=8, max=15)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private final String password;

    public UserRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
