package com.yunstudio.insight.global.security;

import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.jwt.JwtStatus;
import com.yunstudio.insight.global.jwt.JwtUtil;
import com.yunstudio.insight.global.redis.RedisUtil;
import com.yunstudio.insight.global.response.ResultCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j(topic = "logout")
@Component
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String refreshToken = jwtUtil.getRefreshTokenFromCookies(request.getCookies());
        log.info("refresh token : {}", refreshToken);

        validateValidRefreshToken(refreshToken);

        settingLogoutInRedis(refreshToken);
        removeRefreshTokenCookie(response);
    }

    private void validateValidRefreshToken(String refreshToken) {
        JwtStatus refreshTokenStatus = jwtUtil.validateToken(refreshToken);

        if (refreshTokenStatus.equals(JwtStatus.INVALID)) {
            throw new GlobalException(ResultCase.INVALID_TOKEN);
        }
    }

    private void settingLogoutInRedis(String refreshToken) {
        String nickname = jwtUtil.getNicknameFromToken(refreshToken);

        redisUtil.setUserLogout(nickname);
    }

    private void removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie(JwtUtil.REFRESH_TOKEN_HEADER, null);
        response.addCookie(refreshTokenCookie);
    }
}
