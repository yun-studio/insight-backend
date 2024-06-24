package com.yunstudio.insight.domain.answer.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record GetAnswerRes(
    Long id,
    String content,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {

    @QueryProjection // QueryDsl - DTO 변환 어노테이션
    public GetAnswerRes {
    }
}
