package com.yunstudio.insight.domain.answer.service;

import com.yunstudio.insight.domain.answer.dto.request.CreateAnswerReq;
import com.yunstudio.insight.domain.answer.dto.response.CreateAnswerRes;
import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.answer.mapper.AnswerMapper;
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
    private final QuestionRepository questionRepository;

    /**
     * 답변 생성.
     */
    @Transactional
    public CreateAnswerRes createAnswer(User user, Long questionId, CreateAnswerReq request) {

        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new GlobalException(ResultCase.QUESTION_NOT_FOUND));

        Answer answer = Answer.builder()
            .author(user)
            .question(question)
            .content(request.content())
            .build();

        Answer savedAnswer = answerRepository.save(answer);

        return AnswerMapper.INSTANCE.toCreateAnswerRes(savedAnswer);
    }

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
