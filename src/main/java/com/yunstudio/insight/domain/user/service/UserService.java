package com.yunstudio.insight.domain.user.service;

import com.yunstudio.insight.domain.answer.dao.AnswerRepository;
import com.yunstudio.insight.domain.user.dto.request.UserSignupReq;
import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.dto.response.UserChangeNicknameRes;
import com.yunstudio.insight.domain.user.dto.response.UserDeleteRes;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.domain.user.repository.UserRepository;
import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.ResultCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
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
    public CommonEmptyRes signup(UserSignupReq request) {
        // 닉네임이 존재하는지 검증
        validateNickname(request.nickname());

        // 이메일이 존재하는지 검증
        validateEmail(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());
        User.create(request.nickname(), request.email(), encodedPassword);

        return new CommonEmptyRes();
    }

    @Transactional
    public UserChangeNicknameRes changeNickname(User user, String newNickname) {

        // 변경 전 닉네임
        String oldNickname = user.getNickname();

        // 닉네임이 존재하는지 검증
        validateNickname(newNickname);

        // 닉네임 변경
        user.changeNickname(newNickname);
        userRepository.save(user);

        return new UserChangeNicknameRes(oldNickname, newNickname);
    }

    private void validateNickname(String nickname) {
        boolean existsByNickname = userRepository.existsByNickname(nickname);

        if (existsByNickname) {
            throw new GlobalException(ResultCase.DUPLICATED_NICKNAME);
        }
    }

    private void validateEmail(String email) {
        boolean existsByEmail = userRepository.existsByEmail(email);

        if (existsByEmail) {
            throw new GlobalException(ResultCase.DUPLICATED_EMAIL);
        }
    }

    @Transactional
    public UserDeleteRes deleteUser(User user) {

        // 소프트 딜리트
        userRepository.delete(user);

        return new UserDeleteRes(user.getNickname());
    }
}
