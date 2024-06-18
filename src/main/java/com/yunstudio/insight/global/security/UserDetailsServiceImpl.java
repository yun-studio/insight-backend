package com.yunstudio.insight.global.security;

import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.domain.user.repository.UserRepository;
import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.response.ResultCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(username)
            .orElseThrow(() -> new GlobalException(ResultCase.USER_NOT_FOUND));
        
        return new UserDetailsImpl(user);
    }
}
