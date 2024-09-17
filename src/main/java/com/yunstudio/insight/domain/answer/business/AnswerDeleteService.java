package com.yunstudio.insight.domain.answer.business;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.answer.implementation.AnswerReader;
import com.yunstudio.insight.domain.answer.implementation.AnswerRemover;
import com.yunstudio.insight.domain.answer.implementation.AnswerValidator;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerDeleteService {

    private final AnswerReader answerReader;
    private final AnswerRemover answerRemover;
    private final AnswerValidator answerValidator;

    /**
     * 답변 삭제.
     */
    @Transactional
    public CommonEmptyRes deleteAnswer(User user, Long questionId, Long id) {

        Answer answer = answerReader.read(id);

        answerValidator.validateAnswerInQuestion(questionId, answer);
        answerValidator.validateUserIsAuthor(user, answer);

        answerRemover.remove(answer);
        return new CommonEmptyRes();
    }
}
