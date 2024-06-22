package com.yunstudio.insight.domain.question.entity;

import com.yunstudio.insight.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "views", nullable = false)
    private Long views;

    @Column(name = "creator_id")
    private Long creatorId; // userId가 들어가지만, 연관관계는 없음.

    @Builder
    private Question(String content, Long creatorId) {
        this.content = content;
        this.views = 0L;
        this.creatorId = creatorId;
    }

    /**
     * 조회수 증가 메서드
     */
    public void upViews() {
        this.views++;
    }
}
