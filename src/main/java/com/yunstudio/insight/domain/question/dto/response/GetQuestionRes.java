package com.yunstudio.insight.domain.question.dto.response;

import java.time.LocalDateTime;

public record GetQuestionRes(
    Long id,
    String content,
    Long views,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {

}
