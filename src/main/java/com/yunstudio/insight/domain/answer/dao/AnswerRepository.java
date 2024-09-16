package com.yunstudio.insight.domain.answer.dao;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.question.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom {

    Slice<Answer> findByQuestion(Question question, Pageable pageable);
}
