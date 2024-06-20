package com.yunstudio.insight.domain.user.service;

import com.yunstudio.insight.domain.answer.repository.AnswerRepository;
import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.domain.user.repository.UserRepository;
import com.yunstudio.insight.global.jwt.JwtUtil;
import com.yunstudio.insight.global.redis.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    @Transactional(readOnly = true)
    public List<UserAnswerRes> getMyAnswers(User user) {
        return answerRepository.findAllByAuthor(user);
    }

    @Transactional(readOnly = true)
    public List<UserAnswerRes> getLikeAnswers(User user) {
        return answerRepository.findAllUserLikeAnswers(user);
    }

    @Transactional
    public void changeNickname(User user, String newNickname, HttpServletResponse response) {

        // 변경 전 닉네임
        String prevNickname = user.getNickname();
        log.info("변경 전 닉네임 : {}", prevNickname);

        // 닉네임 변경
        user.changeNickname(newNickname);
        User savedUser = userRepository.save(user);

        log.info("변경 완료 닉네임 : {}", savedUser.getNickname());

        // 변경 전 닉네임 로그아웃 처리
        redisUtil.setUserLogout(prevNickname);

        // 변경 후 닉네임으로 토큰 발생
        String accessToken = jwtUtil.setTokenWithBearer(jwtUtil.createAccessToken(newNickname, user.getRole().getAuthority()));
        String refreshToken = jwtUtil.createRefreshToken(newNickname, user.getRole().getAuthority());

        // 리프레쉬 토큰 쿠키 생성
        Cookie refreshTokenCookie = jwtUtil.createRefreshTokenCookie(refreshToken);

        // 응답 객체에 추가
        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, accessToken);
        response.addCookie(refreshTokenCookie);

        // 변경 후 닉네임으로 로그인 처리
        redisUtil.setUserLogin(newNickname, refreshToken);
    }
}
