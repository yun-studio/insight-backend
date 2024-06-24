package com.yunstudio.insight.domain.question.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record GetQuestionsRes(
    Long id,
    String content,
    Long views,
    Integer answerCount,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {

    @QueryProjection
    public GetQuestionsRes {
    }
}
