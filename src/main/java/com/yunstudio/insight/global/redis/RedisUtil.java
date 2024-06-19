package com.yunstudio.insight.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "RedisUtil")
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final LogoutRepository logoutRepository;

    public void setLogout(String nickname, int ttl) {
        Logout logout = Logout.builder()
            .nickname(nickname)
            .ttl(ttl)
            .build();

        logoutRepository.save(logout);
    }

    public void setLogin(String nickname) {
        logoutRepository.findById(nickname)
            .ifPresent(logoutRepository::delete);
    }

    public boolean isLogout(String nickname) {
        return logoutRepository.existsById(nickname);
    }
}
