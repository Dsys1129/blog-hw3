package com.example.bloghw3.user.service;

import com.example.bloghw3.global.BaseResponseDTO;
import com.example.bloghw3.user.dto.LoginResponseDTO;
import com.example.bloghw3.user.dto.RefreshTokenResponseDTO;
import com.example.bloghw3.user.dto.UserRequestDTO;

public interface UserService {

    BaseResponseDTO signup(UserRequestDTO userRequestDTO);

    LoginResponseDTO login(UserRequestDTO userRequestDTO);

    RefreshTokenResponseDTO refreshToken(String refreshToken);
}
