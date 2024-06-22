package com.yunstudio.insight.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;

public record UserUpdateNicknameReq(
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣]{1,35}$", // 영대소문자, 한글 완성자 및 숫자가 1자 이상 35자 이하
        message = "닉네임은 한글 및 숫자와 영어로 1자 이상 35자 이하만 가능합니다."
    )
    String nickname
) {

}
