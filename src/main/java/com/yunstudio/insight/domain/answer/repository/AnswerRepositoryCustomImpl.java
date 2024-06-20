package com.yunstudio.insight.domain.answer.repository;

import static com.yunstudio.insight.domain.answer.entity.QAnswer.answer;
import static com.yunstudio.insight.domain.like.entity.QLike.like;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

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
}
