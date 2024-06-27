package com.yunstudio.insight.domain.question.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.yunstudio.insight.domain.answer.dto.response.GetAnswerRes;
import java.time.LocalDateTime;
import org.springframework.data.domain.Slice;

public record GetQuestionRes(
    Long id,
    String content,
    Long views,
    Slice<GetAnswerRes> answerList,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {

    @QueryProjection // QueryDsl - DTO 변환 어노테이션
    public GetQuestionRes {
    }
}
