package com.yunstudio.insight.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.jwt.JwtUtil;
import com.yunstudio.insight.global.redis.RedisUtil;
import com.yunstudio.insight.global.response.CommonResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * OAuth2 로그인 성공 시, 응답 쿠키에 JWT 추가
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {

        // 유저 파싱
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = principal.getUser();

        // JWT 생성
        String accessToken = jwtUtil.createAccessToken(user.getNickname(), user.getRole().getAuthority());
        String refreshToken = jwtUtil.createRefreshToken(user.getNickname(), user.getRole().getAuthority());

        // 응답 헤더에 Access token 추가
        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, jwtUtil.setTokenWithBearer(accessToken));

        // 응답 쿠키에 Refresh token 추가
        Cookie refreshTokenCookie = jwtUtil.createRefreshTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);

//        // 레디스에 있던 로그아웃 여부 제거하여 로그인 처리
//        redisUtil.setLogin(user.getNickname());

        redisUtil.setUserLogin(user.getNickname(), refreshToken);

        // 응답 바디에 성공 응답 객체 추가
        settingResponseBody(response);
    }

    private void settingResponseBody(HttpServletResponse response) throws IOException {
        // 바디 설정 추가
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Content-Type : application/json
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // charset : utf8

        String json = objectMapper.writeValueAsString(CommonResponse.success());
        response.getWriter().write(json);
    }
}
