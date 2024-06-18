package com.yunstudio.insight.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "RedisUtil")
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Hash 타입으로 닉네임-리프레쉬 토큰 저장하여 로그인 처리
     */
    public void setUserLogin(String nickname, String refreshToken) {
        RefreshToken refreshTokenItem = RefreshToken.builder()
            .nickname(nickname)
            .refreshToken(refreshToken)
            .build();

        refreshTokenRepository.save(refreshTokenItem);
    }

    /**
     * 레디스에 등록된 리프레쉬 토큰을 제거하여 로그아웃 처리
     */
    public void setUserLogout(String nickname) {
        refreshTokenRepository.findById(nickname)
            .ifPresent(refreshTokenRepository::delete);
    }

    /**
     * 레디스에 등록된 리프레쉬 토큰 여부로 로그인 판단
     */
    public boolean isUserLogin(String nickname) {
        return refreshTokenRepository.existsById(nickname);
    }
}
