package com.yunstudio.insight.domain.like.repository;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.like.entity.Like;
import com.yunstudio.insight.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndAnswer(User user, Answer answer);
}
