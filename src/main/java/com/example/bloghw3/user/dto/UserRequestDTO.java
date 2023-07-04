package com.example.bloghw3.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequestDTO {

    @Size(min = 4, max = 10, message = "유저명은 4자 이상 10자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "유저명은 알파벳 소문자와 숫자만 사용할 수 있습니다.")
    private final String username;

    @Size(min=8, max=15, message = "비밀번호는 8자 이상 15자 이하만 가능합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]+$",
            message = "비밀번호는 알파벳 대문자, 소문자, 숫자, 특수문자가 포함되어야 합니다.")
    private final String password;

    public UserRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
