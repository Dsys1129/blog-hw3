package com.example.bloghw3.jwtutil.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.bloghw3.jwtutil.JwtProvider;
import com.example.bloghw3.jwtutil.UserDetails;
import com.example.bloghw3.user.entity.User;
import com.example.bloghw3.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AuthFilter")
@Component
@Order(2)
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpServletRequest.getMethod();
        String url = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(url) &&
                (url.startsWith("/api/signup") || url.startsWith("/api/login") || url.startsWith("/api/reissue") || (url.startsWith("/api/posts") && httpServletRequest.getMethod().equals("GET")) || url.startsWith("/swagger") || url.startsWith("/v3/api-docs") || url.startsWith("/swagger-resources")) || url.startsWith("/h2-console")) {
            // 회원가입, 로그인, 게시글 조회 API 는 인증 필요없이 요청 진행
            chain.doFilter(request, response); // 다음 Filter 로 이동
        } else {
            // 나머지 API 요청은 인증 처리 진행
            // 토큰 확인
            log.info(httpServletRequest.getHeader(JwtProvider.AUTHORIZATION_HEADER));

            String tokenValue = httpServletRequest.getHeader(JwtProvider.AUTHORIZATION_HEADER);

            if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
                // JWT 토큰 substring
                String token = jwtProvider.substringToken(tokenValue);

                // 토큰 검증
                jwtProvider.validateToken(token);

                // 토큰에서 사용자 정보 가져오기
                Claims info = jwtProvider.getUserInfoFromToken(token);

                // jwt 토큰에 subject로 넣어진 유저 네임으로 실제 존재하는 유저인지 확인
                User user = userRepository.findByUsername(info.getSubject()).orElseThrow(() ->
                        new NullPointerException("Not Found User")
                );
                UserDetails userDetails = new UserDetails(user.getUsername(),user.getUserRole());
                log.info("user Detail = {} {}", userDetails.getUsername(), userDetails.getUserRole());
                request.setAttribute("user", userDetails);
                chain.doFilter(request, response); // 다음 Filter 로 이동
            } else {
                throw new IllegalArgumentException("Not Found Token");
            }
        }
    }
}
