package com.example.bloghw3.user.controller;

import com.example.bloghw3.global.BaseResponseDTO;
import com.example.bloghw3.user.dto.RefreshTokenResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.bloghw3.jwtutil.JwtProvider;
import com.example.bloghw3.user.dto.LoginResponseDTO;
import com.example.bloghw3.user.dto.UserRequestDTO;
import com.example.bloghw3.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponseDTO> signup(@Valid @RequestBody UserRequestDTO userRequestDTO){
        BaseResponseDTO response = userService.signup(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO> login(@Valid @RequestBody UserRequestDTO userRequestDTO){
        LoginResponseDTO loginResponse = userService.login(userRequestDTO);
        BaseResponseDTO responseBody = new BaseResponseDTO(loginResponse.getMsg(), loginResponse.getStatus());
        return ResponseEntity.status(HttpStatus.OK)
                .header(JwtProvider.AUTHORIZATION_HEADER, loginResponse.getAccessToken())
                .header(JwtProvider.REFRESH_TOKEN_HEADER, loginResponse.getRefreshToken())
                .body(responseBody);
    }

    @GetMapping("/reissue")
    public ResponseEntity<BaseResponseDTO> refreshToken(@RequestHeader(JwtProvider.REFRESH_TOKEN_HEADER) String refreshToken) {
        RefreshTokenResponseDTO refreshTokenResponseDTO = userService.refreshToken(refreshToken);
        BaseResponseDTO responseBody = new BaseResponseDTO(refreshTokenResponseDTO.getMsg(), refreshTokenResponseDTO.getStatus());
        return ResponseEntity.status(HttpStatus.OK)
                .header(JwtProvider.AUTHORIZATION_HEADER, refreshTokenResponseDTO.getAccessToken())
                .body(responseBody);
    }
}
