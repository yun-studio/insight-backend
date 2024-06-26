package com.yunstudio.insight.domain.answer.dto.response;

import java.time.LocalDateTime;

public record CreateAnswerRes(
    Long id,
    String content,
    Integer likeCount,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {

}
