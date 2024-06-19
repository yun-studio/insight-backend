package com.yunstudio.insight.global.jwt;

import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.redis.RedisUtil;
import com.yunstudio.insight.global.response.ResultCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 검증하는 인증 필터
 */
@Slf4j(topic = "JwtAuthFilter")
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = jwtUtil.getTokenWithoutBearer(request.getHeader(JwtUtil.ACCESS_TOKEN_HEADER));
        log.info("accessToken : {}", accessToken);

        // access token 비어있으면 인증 미처리 후 필터 넘기기
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        JwtStatus accessTokenStatus = jwtUtil.validateToken(accessToken);

        switch (accessTokenStatus) {
            case VALID -> authenticateLoginUser(accessToken);
            case EXPIRED -> authenticateWithRefreshToken(request, response);
            case INVALID -> throw new GlobalException(ResultCase.INVALID_TOKEN);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 로그아웃 여부 확인 후 인증 처리
     */
    private void authenticateLoginUser(String accessToken) {
        // 로그아웃 여부 체크
        if (redisUtil.isLogout(jwtUtil.getNicknameFromToken(accessToken))) {
            throw new GlobalException(ResultCase.LOGIN_REQUIRED);
        }

        setAuthentication(accessToken); // 인증 처리
    }

    private void authenticateWithRefreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = jwtUtil.getRefreshTokenFromCookies(request.getCookies());

        if (refreshToken == null) {
            throw new GlobalException(ResultCase.LOGIN_REQUIRED);
        }

        JwtStatus refreshTokenStatus = jwtUtil.validateToken(refreshToken);

        switch (refreshTokenStatus) {
            case VALID -> setAuthWithRenewAccessToken(response, refreshToken);
            case EXPIRED -> throw new GlobalException(ResultCase.LOGIN_REQUIRED);
            case INVALID -> throw new GlobalException(ResultCase.INVALID_TOKEN);
        }
    }

    /**
     * 재발급한 토큰들을 추가 후 인증 처리
     */
    private void setAuthWithRenewAccessToken(HttpServletResponse response, String refreshToken) {
        String nickname = jwtUtil.getNicknameFromToken(refreshToken);
        String role = jwtUtil.getRoleFromToken(refreshToken);

        // Access token & Refresh token 재발급
        String newAccessToken = jwtUtil.createAccessToken(nickname, role);
        String newRefreshToken = jwtUtil.createRefreshToken(nickname, role);

        // Refresh token 담은 쿠키 생성
        Cookie refreshTokenCookie = createCookie(JwtUtil.REFRESH_TOKEN_HEADER, newRefreshToken, JwtUtil.REFRESH_TOKEN_TTL_SECONDS);

        // 응답 객체에 담기
        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, jwtUtil.setTokenWithBearer(newAccessToken));
        response.addCookie(refreshTokenCookie);

        // 인증 처리
        setAuthentication(refreshToken);
    }

    /**
     * 인증 처리
     */
    private void setAuthentication(String token) {
        String loginId = jwtUtil.getNicknameFromToken(token);
        SecurityContextHolder.getContext().setAuthentication(createAuthentication(loginId));
    }

    /**
     * 인증 객체 생성
     */
    private Authentication createAuthentication(String loginId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    private Cookie createCookie(String cookieName, String cookieValue, int maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);

        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }
}