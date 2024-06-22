package com.yunstudio.insight.domain.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateQuestionReq(
    @NotBlank(message = "질문은 공백이 될 수 없습니다.")
    @Size(min = 1, max = 100, message = "질문의 길이는 1자 이상, 100자 이하 입니다.")
    String content
) {

}
