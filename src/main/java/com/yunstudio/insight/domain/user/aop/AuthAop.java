package com.yunstudio.insight.domain.user.aop;

import com.yunstudio.insight.domain.user.dto.response.UserChangeNicknameRes;
import com.yunstudio.insight.domain.user.dto.response.UserDeleteRes;
import com.yunstudio.insight.domain.user.entity.UserRole;
import com.yunstudio.insight.global.jwt.JwtUtil;
import com.yunstudio.insight.global.redis.RedisUtil;
import com.yunstudio.insight.global.response.CommonResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j(topic = "AuthAop")
@Aspect
@Component
@RequiredArgsConstructor
public class AuthAop {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final HttpServletResponse response;

    @Pointcut("execution(* com.yunstudio.insight.domain.user.controller.UserController.changeNickname(..))")
    private void forChangeNicknameMethod() {
    }

    @Pointcut("execution(* com.yunstudio.insight.domain.user.controller.UserController.deleteUser(..))")
    private void forSoftDeleteUserMethod() {
    }

    @Around("forChangeNicknameMethod()")
    public Object changeNickname(ProceedingJoinPoint joinPoint) throws Throwable {
        // 유저 닉네임 변경 로직 처리
        Object result = joinPoint.proceed();

        // 응답 객체 변환
        CommonResponse<UserChangeNicknameRes> commonResponse = (CommonResponse<UserChangeNicknameRes>) result;
        UserChangeNicknameRes userChangeNicknameRes = commonResponse.getData();

        // 변경 전후 닉네임
        String oldNickname = userChangeNicknameRes.oldNickname();
        String newNickname = userChangeNicknameRes.newNickname();

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

    @Around("forSoftDeleteUserMethod()")
    public Object softDeleteUser(ProceedingJoinPoint joinPoint) throws Throwable {
        // 유저 삭제 로직 처리
        Object result = joinPoint.proceed();

        // 응답 객체 변환
        String softDeletedNickname = getSoftDeleteUserNicknameInResult((CommonResponse<UserDeleteRes>) result);
        log.info("softDeletedNickname : {}", softDeletedNickname);

        // 리프레쉬 토큰 쿠키 삭제
        Cookie removeRefreshTokenCookie = new Cookie(JwtUtil.REFRESH_TOKEN_HEADER, null);
        response.addCookie(removeRefreshTokenCookie);

        // 레디스에서 로그아웃 처리
        redisUtil.setUserLogout(softDeletedNickname);

        return result;
    }

    private String getSoftDeleteUserNicknameInResult(CommonResponse<UserDeleteRes> result) {
        UserDeleteRes userDeleteRes = result.getData();
        return userDeleteRes.nickname();
    }
}
