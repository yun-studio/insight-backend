package com.yunstudio.insight.domain.question.repository;

import com.yunstudio.insight.domain.question.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {

    List<Question> findAllByContentContainingIgnoreCase(String content);
}
