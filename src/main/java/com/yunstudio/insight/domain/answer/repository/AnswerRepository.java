package com.yunstudio.insight.domain.answer.repository;

import com.yunstudio.insight.domain.answer.entity.Answer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom {

    @Query(value = "select a from Answer a join fetch a.author u where a.id = :id")
    Optional<Answer> findByIdWithAuthor(Long id);
}
