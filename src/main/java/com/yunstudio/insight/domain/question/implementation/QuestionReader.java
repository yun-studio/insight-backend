package com.yunstudio.insight.domain.question.implementation;

import com.yunstudio.insight.domain.question.entity.Question;
import com.yunstudio.insight.domain.question.repository.QuestionRepository;
import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.response.ResultCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionReader {

    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public Question read(Long questionId) {

        return questionRepository.findById(questionId)
            .orElseThrow(() -> new GlobalException(ResultCase.QUESTION_NOT_FOUND));
    }
}
