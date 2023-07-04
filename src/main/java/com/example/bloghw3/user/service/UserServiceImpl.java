package com.example.bloghw3.user.service;

import java.util.Optional;

import com.example.bloghw3.user.dto.RefreshTokenResponseDTO;
import com.example.bloghw3.user.entity.RefreshToken;
import com.example.bloghw3.user.exception.RefreshTokenExpiredException;
import com.example.bloghw3.user.repository.RefreshTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloghw3.jwtutil.JwtProvider;
import com.example.bloghw3.user.dto.LoginResponseDTO;
import com.example.bloghw3.user.dto.UserRequestDTO;
import com.example.bloghw3.user.dto.UserResponseDTO;
import com.example.bloghw3.user.entity.User;
import com.example.bloghw3.user.exception.PasswordMismatchException;
import com.example.bloghw3.user.exception.UserDuplicationException;
import com.example.bloghw3.user.exception.UserNotFoundException;
import com.example.bloghw3.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public UserResponseDTO signup(UserRequestDTO userRequestDTO) {
        Optional<User> findUser = userRepository.findByUsername(userRequestDTO.getUsername());

        if (findUser.isPresent()){
            throw new UserDuplicationException("아이디 중복");
        }
        User user = User.builder()
            .username(userRequestDTO.getUsername())
            .password(passwordEncoder.encode(userRequestDTO.getPassword()))
            .build();
        userRepository.save(user);

        return new UserResponseDTO("true",201);
    }

    @Transactional
    @Override
    public LoginResponseDTO login(UserRequestDTO userRequestDTO) {
        String username = userRequestDTO.getUsername();
        String rawPassword = userRequestDTO.getPassword();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("등록된 사용자가 아닙니다."));

        if (!passwordEncoder.matches(rawPassword,user.getPassword())){
            throw new PasswordMismatchException("비밀번호 오류");
        }
        String accessToken = jwtProvider.createToken(username);
        String refreshToken = jwtProvider.createRefreshToken(username);

        Optional<RefreshToken> exitRefreshToken = refreshTokenRepository.findById(user.getId());

        if(exitRefreshToken.isPresent()) {
            exitRefreshToken.get().updateToken(jwtProvider.substringToken(refreshToken));
        } else {
            RefreshToken newRefreshToken = new RefreshToken(user, jwtProvider.substringToken(refreshToken));
            refreshTokenRepository.save(newRefreshToken);
        }

        return new LoginResponseDTO("true",200, accessToken, refreshToken);
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(String refreshToken) {
        String token = jwtProvider.substringToken(refreshToken);
        jwtProvider.validateToken(token);
        Optional<RefreshToken> exitRefreshToken = refreshTokenRepository.findByRefreshToken(token);
        if(exitRefreshToken.isPresent()) {
            Optional<User> user = userRepository.findById(exitRefreshToken.get().getUser().getId());
            if(user.isPresent()) {
                String accessToken = jwtProvider.createToken(user.get().getUsername());
                return new RefreshTokenResponseDTO("true", 200, accessToken);
            }
            throw new UserNotFoundException("존재하지 않는 회원입니다.");
        } else {
            throw new RefreshTokenExpiredException("Refresh token이 만료되었습니다. 로그인이 필요합니다.");
        }
    }
}
