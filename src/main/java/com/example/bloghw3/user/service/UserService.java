package com.example.bloghw3.user.service;

import com.example.bloghw3.user.dto.LoginResponseDTO;
import com.example.bloghw3.user.dto.RefreshTokenResponseDTO;
import com.example.bloghw3.user.dto.UserRequestDTO;
import com.example.bloghw3.user.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO signup(UserRequestDTO userRequestDTO);

    LoginResponseDTO login(UserRequestDTO userRequestDTO);

    RefreshTokenResponseDTO refreshToken(String refreshToken);
}
