package com.yunstudio.insight.domain.answer.business;

import com.yunstudio.insight.domain.answer.dto.request.CreateAnswerReq;
import com.yunstudio.insight.domain.answer.dto.response.CreateAnswerRes;
import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.answer.implementation.AnswerReader;
import com.yunstudio.insight.domain.answer.implementation.AnswerRemover;
import com.yunstudio.insight.domain.answer.implementation.AnswerSaver;
import com.yunstudio.insight.domain.answer.implementation.AnswerValidator;
import com.yunstudio.insight.domain.answer.mapper.AnswerMapper;
import com.yunstudio.insight.domain.question.entity.Question;
import com.yunstudio.insight.domain.question.implementation.QuestionReader;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerSaver answerSaver;
    private final AnswerReader answerReader;
    private final AnswerMapper answerMapper;
    private final AnswerRemover answerRemover;
    private final QuestionReader questionReader;
    private final AnswerValidator answerValidator;

    /**
     * 답변 생성.
     */
    @Transactional
    public CreateAnswerRes createAnswer(User user, Long questionId, CreateAnswerReq request) {

        Question question = questionReader.read(questionId);
        Answer answer = answerSaver.save(user, question, request.content());

        return answerMapper.toCreateAnswerRes(answer);
    }

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
