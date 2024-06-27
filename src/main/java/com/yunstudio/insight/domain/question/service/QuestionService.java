package com.yunstudio.insight.domain.question.service;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.answer.repository.AnswerRepository;
import com.yunstudio.insight.domain.question.dto.request.CreateQuestionReq;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionRes;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionsRes;
import com.yunstudio.insight.domain.question.entity.Question;
import com.yunstudio.insight.domain.question.mapper.QuestionMapper;
import com.yunstudio.insight.domain.question.repository.QuestionRepository;
import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.ResultCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    /**
     * 질문 목록 조회.
     */
    @Transactional(readOnly = true)
    public Slice<GetQuestionsRes> getQuestions(String query, Pageable pageable) {

        return questionRepository.findAllByQueryPaging(query, pageable)
            .map(QuestionMapper.INSTANCE::toGetQuestionsRes);
    }

    /**
     * 질문 단건 조회. 조회수 증가 처리 필요.
     */
    @Transactional
    public GetQuestionRes getQuestion(Long id, Pageable pageable) {
        Question question = questionRepository.findByIdWithPessimisticLock(id)
            .orElseThrow(() -> new GlobalException(ResultCase.QUESTION_NOT_FOUND));

        Slice<Answer> answerList = answerRepository.findByQuestion(question, pageable);

        question.upViews(); // 조회수 증가
        Question savedQuestion = questionRepository.save(question);

        return QuestionMapper.INSTANCE.toGetQuestionRes(savedQuestion, answerList);
    }

    /**
     * 질문 추가.
     */
    @Transactional
    public CommonEmptyRes createQuestion(Long creatorId, CreateQuestionReq request) {
        Question question = Question.builder()
            .creatorId(creatorId)
            .content(request.content())
            .build();

        questionRepository.save(question);

        return new CommonEmptyRes();
    }
}
