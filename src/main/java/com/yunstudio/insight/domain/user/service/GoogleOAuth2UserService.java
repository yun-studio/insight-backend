package com.yunstudio.insight.domain.user.service;

import com.yunstudio.insight.domain.user.entity.OAuthProvider;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.domain.user.repository.UserRepository;
import com.yunstudio.insight.global.security.UserDetailsImpl;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        User user = getUser(oAuth2User);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        return new UserDetailsImpl(user, attributes);
    }

    private User getUser(OAuth2User oAuth2User) {
        String providerId = oAuth2User.getAttribute("sub");

        return userRepository.findByProviderId(providerId)
            .orElseGet(() -> getNewUser(oAuth2User));
    }

    private User getNewUser(OAuth2User oAuth2User) {

        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String nickname = oAuth2User.getAttribute("name");
        String profileUrl = oAuth2User.getAttribute("picture");

        return userRepository.save(
            User.builder()
                .email(email)
                .nickname(nickname)
                .provider(OAuthProvider.GOOGLE)
                .providerId(providerId)
                .password(UUID.randomUUID().toString().replace("-", ""))
                .profileUrl(profileUrl)
                .build());
    }
}
