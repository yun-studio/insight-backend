package com.yunstudio.insight.domain.answer.implementation;

import com.yunstudio.insight.domain.answer.dao.AnswerRepository;
import com.yunstudio.insight.domain.answer.entity.Answer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerRemover {

    private final AnswerRepository answerRepository;

    @Transactional
    public void remove(Long answerId) {
        answerRepository.deleteById(answerId);
    }

    @Transactional
    public void remove(Answer answer) {
        answerRepository.delete(answer);
    }
}
