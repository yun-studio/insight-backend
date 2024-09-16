package com.yunstudio.insight.domain.answer.implementation;

import com.yunstudio.insight.domain.answer.dao.AnswerRepository;
import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.response.ResultCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerReader {

    private final AnswerRepository answerRepository;

    @Transactional(readOnly = true)
    public Answer read(Long answerId) {

        return answerRepository.findById(answerId)
            .orElseThrow(() -> new GlobalException(ResultCase.ANSWER_NOT_FOUND));
    }
}
