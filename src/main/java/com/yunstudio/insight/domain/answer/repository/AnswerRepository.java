package com.yunstudio.insight.domain.answer.repository;

import com.yunstudio.insight.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom {

}
