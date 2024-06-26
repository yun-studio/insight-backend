package com.yunstudio.insight.domain.answer.service;

import com.yunstudio.insight.domain.answer.dto.request.CreateAnswerReq;
import com.yunstudio.insight.domain.answer.dto.response.CreateAnswerRes;
import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.answer.mapper.AnswerMapper;
import com.yunstudio.insight.domain.answer.repository.AnswerRepository;
import com.yunstudio.insight.domain.question.entity.Question;
import com.yunstudio.insight.domain.question.repository.QuestionRepository;
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
    public CommonEmptyRes deleteAnswer(User user, Long questionId, Long id) {
        
        Answer answer = answerRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ResultCase.ANSWER_NOT_FOUND));

        // 답변이 요청한 질문 안에 있는지 체크
        if (!isAnswerInQuestion(questionId, answer)) {
            throw new GlobalException(ResultCase.NOT_AUTHORIZED);
        }

        // 답변 작성자가 로그인 유저인지 체크
        if (!isUserAuthor(user, answer)) {
            throw new GlobalException(ResultCase.NOT_AUTHORIZED);
        }

        answerRepository.delete(answer);

        return new CommonEmptyRes();
    }

    private boolean isAnswerInQuestion(Long questionId, Answer answer) {
        return answer.getQuestion().getId().equals(questionId);
    }

    private boolean isUserAuthor(User user, Answer answer) {
        return answer.getAuthor().getId().equals(user.getId());
    }
}
