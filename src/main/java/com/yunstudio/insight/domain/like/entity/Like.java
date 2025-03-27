package com.yunstudio.insight.domain.like.entity;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.model.BaseEntity;
import com.yunstudio.insight.domain.user.entity.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {

    @EmbeddedId
    private LikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("answerId")
    @JoinColumn(name = "answer_id")
    private Answer answer;

    public static Like create(User user, Answer answer) {
        Like like = new Like();

        like.user = user;
        like.answer = answer;
        like.id = new LikeId(user, answer);

        return like;
    }
}
