package com.yunstudio.insight.domain.question.repository;

import com.yunstudio.insight.domain.question.entity.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "select q from Question q join fetch q.answerList a where q.id = :id")
    Optional<Question> findById(Long id);

    List<Question> findAllByContentContainingIgnoreCase(String content);
}
