package com.yunstudio.insight.domain.answer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAnswerReq(
    @NotBlank(message = "답변은 공백이 될 수 없습니다.")
    @Size(min = 1, max = 10000, message = "답변의 최대 글자수는 10,000자 이하입니다.")
    String content
) {

}
