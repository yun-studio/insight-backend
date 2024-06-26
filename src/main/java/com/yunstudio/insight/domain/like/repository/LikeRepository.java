package com.yunstudio.insight.domain.like.repository;

import com.yunstudio.insight.domain.like.entity.Like;
import com.yunstudio.insight.domain.like.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

}
