package com.yunstudio.insight.domain.question.repository;

import static com.yunstudio.insight.domain.question.entity.QQuestion.question;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionsRes;
import com.yunstudio.insight.domain.question.dto.response.QGetQuestionsRes;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetQuestionsRes> findAllByQuery(String query) {

        // 질문 응답 DTO
        QGetQuestionsRes qGetQuestionsRes = new QGetQuestionsRes(
            question.id,
            question.content,
            question.views,
            question.createdAt,
            question.modifiedAt
        );

        return jpaQueryFactory.select(qGetQuestionsRes)
            .from(question)
            .where(question.content.containsIgnoreCase(query))
            .fetch();
    }
}
