package com.yunstudio.insight.domain.question.repository;

import static com.yunstudio.insight.domain.answer.entity.QAnswer.answer;
import static com.yunstudio.insight.domain.question.entity.QQuestion.question;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunstudio.insight.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Question> findAllByQueryPaging(String content, Pageable pageable) {

        BooleanExpression predicate = question.content.upper().like("%" + content.toUpperCase() + "%");

        JPAQuery<Question> query = queryFactory
            .selectFrom(question)
            .leftJoin(question.answerList, answer).fetchJoin()
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        // 정렬 조건 추가
        for (Sort.Order order : pageable.getSort()) {
            query.orderBy(createOrderSpecifier(order));
        }

        query.orderBy(question.createdAt.desc());

        return new SliceImpl<>(query.fetch());
    }

    private OrderSpecifier<?> createOrderSpecifier(Sort.Order order) {
        String sortType = order.getProperty();
        Order orderDirection = order.isAscending() ? Order.ASC : Order.DESC;

        return switch (sortType) {
            case "createdAt" -> new OrderSpecifier<>(orderDirection, question.createdAt);
            case "answerCount" -> new OrderSpecifier<>(orderDirection, question.answerList.size());
            case "views" -> new OrderSpecifier<>(orderDirection, question.views);
            default -> new OrderSpecifier<>(Order.DESC, question.createdAt);
        };
    }
}
