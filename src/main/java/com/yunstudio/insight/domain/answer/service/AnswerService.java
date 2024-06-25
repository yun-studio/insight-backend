package com.yunstudio.insight.domain.answer.service;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.answer.repository.AnswerRepository;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.ResultCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    /**
     * 답변 삭제.
     */
    @Transactional
    public CommonEmptyRes deleteAnswer(User user, Long id) {
        Answer answer = answerRepository.findByIdWithAuthor(id)
            .orElseThrow(() -> new GlobalException(ResultCase.ANSWER_NOT_FOUND));

        if (!isUserAuthor(user, answer)) {
            throw new GlobalException(ResultCase.NOT_AUTHORIZED);
        }

        answerRepository.delete(answer);

        return new CommonEmptyRes();
    }

    private boolean isUserAuthor(User user, Answer answer) {
        return answer.getAuthor().getId().equals(user.getId());
    }
}
