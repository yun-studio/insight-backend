package com.yunstudio.insight.domain.question.repository;

import com.yunstudio.insight.domain.question.entity.Question;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select q from Question q join fetch q.answerList a where q.id = :id")
    Optional<Question> findByIdWithPessimisticLock(Long id);
}
