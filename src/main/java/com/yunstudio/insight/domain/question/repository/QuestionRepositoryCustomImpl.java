package com.yunstudio.insight.domain.question.repository;

import static com.yunstudio.insight.domain.answer.entity.QAnswer.answer;
import static com.yunstudio.insight.domain.question.entity.QQuestion.question;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunstudio.insight.domain.question.entity.Question;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Question> findAllByQueryPaging(String content, Pageable pageable) {

        BooleanExpression predicate = question.content.upper().like("%" + content.toUpperCase() + "%");

        List<Question> results = queryFactory
            .selectFrom(question)
            .leftJoin(question.answerList, answer).fetchJoin()
            .where(predicate)
            .orderBy(question.answerList.size().asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new SliceImpl<>(results);
    }
}
