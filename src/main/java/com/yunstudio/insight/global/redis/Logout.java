package com.yunstudio.insight.global.redis;

import java.util.concurrent.TimeUnit;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@RedisHash(value = "logout")
public class Logout {

    @Id
    private String nickname;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private int ttl;
}
