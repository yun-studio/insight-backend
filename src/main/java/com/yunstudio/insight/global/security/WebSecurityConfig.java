package com.yunstudio.insight.global.security;

//import com.yunstudio.insight.domain.user.service.GoogleOAuth2UserService;

import com.yunstudio.insight.global.exception.ExceptionHandlerFilter;
import com.yunstudio.insight.global.jwt.JwtAuthFilter;
import com.yunstudio.insight.global.jwt.JwtUtil;
import com.yunstudio.insight.global.redis.RedisUtil;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsService userDetailsService;
    //    private final GoogleOAuth2UserService googleOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final LogoutHandler logoutHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtUtil, redisUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정
        http.csrf(AbstractHttpConfigurer::disable);

        // CORS 설정
        http.cors(getCorsConfigurerCustomizer());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//        // OAuth2 Login 설정
//        http.oauth2Login(getoAuth2LoginConfigurerCustomizer());

        // Filter 순서 설정
        settingFilterOrder(http);

        // 로그아웃 설정
        settingLogout(http);

        // 요청 URL 접근 설정
        settingRequestAuthorization(http);

        return http.build();
    }

    /**
     * CORS 설정
     */
    private Customizer<CorsConfigurer<HttpSecurity>> getCorsConfigurerCustomizer() {
        return corsConfigurer -> corsConfigurer.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("*")); // 모든 오리진 허용
            config.setAllowedMethods(Collections.singletonList("*")); // 모든 메서드 허용
            config.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
            config.setExposedHeaders(Collections.singletonList("*"));
            return config;
        });
    }

//    /**
//     * OAuth2 Login 설정
//     */
//    private Customizer<OAuth2LoginConfigurer<HttpSecurity>> getoAuth2LoginConfigurerCustomizer() {
//        return oAuth2LoginConfig -> oAuth2LoginConfig
//            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(googleOAuth2UserService))
//            .successHandler(oAuth2SuccessHandler);
//    }

    /**
     * Filter 설정
     */
    private void settingFilterOrder(HttpSecurity http) {
        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(exceptionHandlerFilter(), LogoutFilter.class);
    }

    /**
     * 요청 URL 접근 설정
     */
    private void settingRequestAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz ->
            authz
                // 정적 파일
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // 유저 도메인
                .requestMatchers("/login/oauth2/code/google").permitAll() // 구글 로그인 및 회원가입
                .requestMatchers("/users/signup").permitAll() // 회원가입
                .requestMatchers(HttpMethod.POST, "/users/logout").authenticated() // 로그아웃
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/users/dummy").permitAll()
                .requestMatchers("/users/test").permitAll()
                // Question 도메인
                .requestMatchers(HttpMethod.GET, "/questions/**").permitAll()
                // 그 외
                .anyRequest().authenticated()
        );
    }

    /**
     * 로그아웃 설정 기본 로그아웃 핸들러가 SecurityContext 초기화, 쿠키 제거 진행
     */
    private void settingLogout(HttpSecurity http) throws Exception {
        http.logout(
            logout -> {
                logout.logoutUrl("/users/logout");
                logout.addLogoutHandler(logoutHandler);
                logout.deleteCookies(JwtUtil.REFRESH_TOKEN_HEADER); // 지정된 쿠키 삭제
                logout.logoutSuccessHandler(logoutSuccessHandler);
            });
    }
}
