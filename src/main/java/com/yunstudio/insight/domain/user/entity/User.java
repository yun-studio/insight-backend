package com.yunstudio.insight.domain.user.entity;

import com.yunstudio.insight.domain.model.SoftDeleteEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false, unique = true, length = 25) // 구글 이름 최대 글자는 25
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 60) // BCrypt 길이는 60
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "profile_url")
    private String profileUrl;

    @Builder
    private User(String nickname, String email, String password, OAuthProvider provider, String providerId, String profileUrl) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
        this.provider = provider;
        this.providerId = providerId;
    }
}
