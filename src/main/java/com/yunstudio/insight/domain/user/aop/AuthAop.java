package com.yunstudio.insight.domain.user.aop;

import com.yunstudio.insight.domain.user.dto.response.UserDeleteRes;
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

    private final RedisUtil redisUtil;
    private final HttpServletResponse response;

    @Pointcut("execution(* com.yunstudio.insight.domain.user.controller.UserController.deleteUser(..))")
    private void forSoftDeleteUserMethod() {
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
