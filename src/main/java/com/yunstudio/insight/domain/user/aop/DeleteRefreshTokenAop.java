package com.yunstudio.insight.domain.user.aop;


import com.yunstudio.insight.domain.user.dto.response.UserDeleteRes;
import com.yunstudio.insight.global.jwt.JwtUtil;
import com.yunstudio.insight.global.redis.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DeleteRefreshTokenAop {

    private final RedisUtil redisUtil;
    private final HttpServletResponse response;

    @AfterReturning(value = "@annotation(com.yunstudio.insight.domain.user.aop.annotation.DeleteRefreshTokenInRedis)", returning = "result")
    public Object softDeleteUser(JoinPoint joinPoint, Object result) {

        // 응답 객체 변환
        UserDeleteRes res = (UserDeleteRes) result;
        String softDeletedNickname = res.nickname();
        log.info("softDeletedNickname : {}", softDeletedNickname);

        // 리프레쉬 토큰 쿠키 삭제
        Cookie removeRefreshTokenCookie = new Cookie(JwtUtil.REFRESH_TOKEN_HEADER, null);
        response.addCookie(removeRefreshTokenCookie);

        // 레디스에서 로그아웃 처리
        redisUtil.setUserLogout(softDeletedNickname);

        return result;
    }
}
