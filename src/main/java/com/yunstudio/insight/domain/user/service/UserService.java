package com.yunstudio.insight.domain.user.service;

import com.yunstudio.insight.domain.answer.repository.AnswerRepository;
import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AnswerRepository answerRepository;

    @Transactional(readOnly = true)
    public List<UserAnswerRes> getMyAnswers(User user) {
        return answerRepository.findAllByAuthor(user);
    }

    @Transactional(readOnly = true)
    public List<UserAnswerRes> getLikeAnswers(User user) {
        return answerRepository.findAllUserLikeAnswers(user);
    }
}
