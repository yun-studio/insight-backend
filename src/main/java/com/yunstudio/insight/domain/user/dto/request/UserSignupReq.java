package com.yunstudio.insight.domain.user.dto.request;

public record UserSignupReq(
    String email,
    String nickname,
    String password
) {

}
