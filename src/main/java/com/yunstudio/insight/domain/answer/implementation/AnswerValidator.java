package com.yunstudio.insight.domain.answer.implementation;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.response.ResultCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerValidator {

    /**
     * 답변이 요청한 질문 안에 있는지 체크
     */
    public void validateAnswerInQuestion(Long questionId, Answer answer) {
        if (!isAnswerInQuestion(questionId, answer)) {
            throw new GlobalException(ResultCase.NOT_AUTHORIZED);
        }
    }

    private boolean isAnswerInQuestion(Long questionId, Answer answer) {
        return answer.getQuestion().getId().equals(questionId);
    }

    /**
     * 답변 작성자가 로그인 유저인지 체크
     */
    public void validateUserIsAuthor(User user, Answer answer) {
        if (!isUserAuthor(user, answer)) {
            throw new GlobalException(ResultCase.NOT_AUTHORIZED);
        }
    }

    public boolean isUserAuthor(User user, Answer answer) {
        return answer.getAuthor().getId().equals(user.getId());
    }
}
