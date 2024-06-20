package com.yunstudio.insight.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserChangeNicknameRes(
    String oldNickname,
    String newNickname
) {

}
