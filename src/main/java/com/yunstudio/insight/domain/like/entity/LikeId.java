package com.yunstudio.insight.domain.like.entity;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "answer_id")
    private Long answerId;

    public static LikeId of(User user, Answer answer) {
        LikeId likeId = new LikeId();

        likeId.userId = user.getId();
        likeId.answerId = answer.getId();

        return likeId;
    }
}
