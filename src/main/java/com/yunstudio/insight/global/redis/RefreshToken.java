package com.yunstudio.insight.global.redis;

import com.yunstudio.insight.global.jwt.JwtUtil;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = JwtUtil.REFRESH_TOKEN_TTL_SECONDS)
@Builder
public class RefreshToken {

    @Id
    private String nickname;
    private String refreshToken;
}
