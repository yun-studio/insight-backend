package com.yunstudio.insight.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN(Authority.ADMIN, "관리자"),
    USER(Authority.USER, "사용자");

    private final String authority;
    private final String value;

    public static class Authority {

        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
