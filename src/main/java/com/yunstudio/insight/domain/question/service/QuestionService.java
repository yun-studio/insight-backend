package com.yunstudio.insight.domain.question.service;

import com.yunstudio.insight.domain.question.dto.request.CreateQuestionReq;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionRes;
import com.yunstudio.insight.domain.question.entity.Question;
import com.yunstudio.insight.domain.question.mapper.QuestionMapper;
import com.yunstudio.insight.domain.question.repository.QuestionRepository;
import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.ResultCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * 질문 목록 조회.
     */
    @Transactional(readOnly = true)
    public List<GetQuestionRes> getQuestions(String query) {

        return questionRepository.findAllByQuery(query)
            .stream()
            .map(QuestionMapper.INSTANCE::toGetQuestionRes)
            .toList();
    }

    /**
     * 질문 단건 조회. 조회수 증가 처리 필요.
     */
    @Transactional
    public GetQuestionRes getQuestion(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ResultCase.QUESTION_NOT_FOUND));

        question.upViews(); // 조회수 증가

        return QuestionMapper.INSTANCE.toGetQuestionRes(questionRepository.save(question));
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
