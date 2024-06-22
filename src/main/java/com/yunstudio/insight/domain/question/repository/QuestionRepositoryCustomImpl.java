package com.yunstudio.insight.domain.question.repository;

import static com.yunstudio.insight.domain.question.entity.QQuestion.question;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunstudio.insight.domain.question.entity.Question;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Question> findAllByQuery(String query) {
        return jpaQueryFactory.selectFrom(question)
            .where(question.content.containsIgnoreCase(query))
            .fetch();
    }
}
