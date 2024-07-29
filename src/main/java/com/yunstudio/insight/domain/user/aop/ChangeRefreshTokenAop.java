package com.yunstudio.insight.domain.user.aop;

import com.yunstudio.insight.domain.user.dto.response.UserChangeNicknameRes;
import com.yunstudio.insight.domain.user.entity.UserRole;
import com.yunstudio.insight.global.jwt.JwtUtil;
import com.yunstudio.insight.global.redis.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ChangeRefreshTokenAop {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final HttpServletResponse response;

    @AfterReturning(value = "@annotation(com.yunstudio.insight.domain.user.aop.annotation.ChangeRefreshTokenInRedis)", returning = "result")
    public UserChangeNicknameRes changeNickname(UserChangeNicknameRes result) {

        // 변경 전후 닉네임
        String oldNickname = result.oldNickname();
        String newNickname = result.newNickname();

        log.info("변경 전 닉네임 : {}", oldNickname);
        log.info("변경 후 닉네임 : {}", newNickname);

        // 변경 전 닉네임 로그아웃 처리
        redisUtil.setUserLogout(oldNickname);

        // 액세스 토큰 처리
        addAccessTokenInHeader(newNickname);

        // 리프레쉬 토큰 처리
        changeRefreshToken(newNickname);

        return result;
    }

    private void addAccessTokenInHeader(String newNickname) {
        // 변경 후 닉네임으로 액세스 토큰 발생
        String accessToken = jwtUtil.setTokenWithBearer(jwtUtil.createAccessToken(newNickname, UserRole.USER.getAuthority()));

        // 응답 객체에 추가
        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, accessToken);
    }

    private void changeRefreshToken(String newNickname) {
        // 변경 후 닉네임으로 리프레쉬 토큰 발생
        String refreshToken = jwtUtil.createRefreshToken(newNickname, UserRole.USER.getAuthority());

        // 리프레쉬 토큰 쿠키 생성
        Cookie refreshTokenCookie = jwtUtil.createRefreshTokenCookie(refreshToken);

        // 응답 객체에 쿠키 추가
        response.addCookie(refreshTokenCookie);

        // 변경 후 닉네임으로 레디스 로그인 처리
        redisUtil.setUserLogin(newNickname, refreshToken);
    }
}
