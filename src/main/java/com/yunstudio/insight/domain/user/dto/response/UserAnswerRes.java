package com.yunstudio.insight.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserAnswerRes(
    Long id,
    String content,
    Long userId,
    Long questionId,
    boolean isLike
) {

}
