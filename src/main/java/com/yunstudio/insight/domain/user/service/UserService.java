package com.yunstudio.insight.domain.user.service;

import com.yunstudio.insight.domain.answer.repository.AnswerRepository;
import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.dto.response.UserChangeNicknameRes;
import com.yunstudio.insight.domain.user.dto.response.UserDeleteRes;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

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
    public UserChangeNicknameRes changeNickname(User user, String newNickname) {

        // 변경 전 닉네임
        String oldNickname = user.getNickname();

        // 닉네임 변경
        user.changeNickname(newNickname);
        userRepository.save(user);

        return new UserChangeNicknameRes(oldNickname, newNickname);
    }

    @Transactional
    public UserDeleteRes deleteUser(User user) {

        // 소프트 딜리트
        user.softDelete();
        userRepository.save(user);

        return new UserDeleteRes(user.getNickname());
    }
}
