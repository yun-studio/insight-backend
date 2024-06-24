package com.yunstudio.insight.domain.question.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.yunstudio.insight.domain.answer.entity.QAnswer.answer;
import static com.yunstudio.insight.domain.question.entity.QQuestion.question;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunstudio.insight.domain.answer.dto.response.QGetAnswerRes;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionRes;
import com.yunstudio.insight.domain.question.dto.response.QGetQuestionRes;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetQuestionRes> findAllByQuery(String query) {

        // 답변 응답 DTO
        QGetAnswerRes qGetAnswerRes = new QGetAnswerRes(answer.id, answer.content, answer.createdAt, answer.modifiedAt);

        // 질문 응답 DTO
        QGetQuestionRes qGetQuestionRes = new QGetQuestionRes(
            question.id,
            question.content,
            question.views,
            list(qGetAnswerRes.skipNulls()),
            question.createdAt,
            question.modifiedAt
        );

        return jpaQueryFactory.select(qGetQuestionRes)
            .from(question)
            .leftJoin(answer)
            .on(question.id.eq(answer.question.id))
            .where(question.content.containsIgnoreCase(query))
            .transform(groupBy(question.id).list(qGetQuestionRes));
    }
}
