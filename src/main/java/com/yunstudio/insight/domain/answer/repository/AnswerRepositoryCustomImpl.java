package com.yunstudio.insight.domain.answer.repository;

import static com.yunstudio.insight.domain.answer.entity.QAnswer.answer;
import static com.yunstudio.insight.domain.like.entity.QLike.like;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class AnswerRepositoryCustomImpl implements AnswerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserAnswerRes> findAllUserLikeAnswers(User user) {
        return jpaQueryFactory.select(
                Projections.constructor(UserAnswerRes.class, answer.id, answer.content, answer.author.id, answer.question.id,
                    like.user.eq(user)))
            .from(answer)
            .innerJoin(like)
            .on(answer.eq(like.answer))
            .where(like.user.eq(user))
            .fetch();
    }

    @Override
    public List<UserAnswerRes> findAllByAuthor(User author) {
        return jpaQueryFactory.select(
                Projections.constructor(UserAnswerRes.class, answer.id, answer.content, answer.author.id, answer.question.id,
                    like.user.eq(author)))
            .from(answer)
            .leftJoin(like)
            .on(answer.eq(like.answer))
            .where(answer.author.eq(author))
            .fetch();
    }

    @Override
    public Slice<Answer> findAllByQuestionPaging(Long questionId, Pageable pageable) {

        JPAQuery<Answer> query = jpaQueryFactory.selectFrom(answer)
            .leftJoin(answer.likeList).fetchJoin()
            .where(answer.question.id.eq(questionId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        for (Sort.Order order : pageable.getSort()) {
            query.orderBy(createOrderSpecifier(order));
        }

        query.orderBy(answer.createdAt.desc());

        return new SliceImpl<>(query.fetch());
    }

    private OrderSpecifier<?> createOrderSpecifier(Sort.Order order) {
        String sortType = order.getProperty();
        Order orderDirection = order.isAscending() ? Order.ASC : Order.DESC;

        return switch (sortType) {
            case "createdAt" -> new OrderSpecifier<>(orderDirection, answer.createdAt);
            case "likeCount" -> new OrderSpecifier<>(orderDirection, answer.likeList.size());
            default -> new OrderSpecifier<>(Order.DESC, answer.createdAt);
        };
    }
}
