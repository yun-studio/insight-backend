package com.yunstudio.insight.domain.question.entity;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
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

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "views", nullable = false)
    private Long views;

    @Column(name = "creator_id")
    private Long creatorId; // userId가 들어가지만, 연관관계는 없음.

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @OneToMany(mappedBy = "question")
    private List<Answer> answerList = new ArrayList<>();

    public static Question create(String content, Long creatorId) {
        Question question = new Question();

        question.content = content;
        question.views = 0L;
        question.creatorId = creatorId;
        question.isConfirmed = false;

        return question;
    }

    /**
     * 조회수 증가 메서드
     */
    public void upViews() {
        this.views++;
    }
}
