package com.yunstudio.insight.domain.answer.implementation;

import com.yunstudio.insight.domain.answer.dao.AnswerRepository;
import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.question.entity.Question;
import com.yunstudio.insight.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerSaver {

    private final AnswerRepository answerRepository;

    @Transactional
    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

    @Transactional
    public Answer save(User user, Question question, String content) {
        Answer answer = Answer.create(content, user, question);
        return answerRepository.save(answer);
    }
}
