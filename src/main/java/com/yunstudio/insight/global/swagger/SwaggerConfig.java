package com.yunstudio.insight.global.swagger;

import com.yunstudio.insight.global.jwt.JwtUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    public OpenAPI openAPI() {

        SecurityRequirement accessTokenRequirement = new SecurityRequirement().addList(JwtUtil.ACCESS_TOKEN_HEADER);
        SecurityRequirement refreshTokenRequirement = new SecurityRequirement().addList(JwtUtil.REFRESH_TOKEN_HEADER);

        Components components = new Components()
            .addSecuritySchemes(JwtUtil.ACCESS_TOKEN_HEADER, accessTokenSecurityScheme())
            .addSecuritySchemes(JwtUtil.REFRESH_TOKEN_HEADER, refreshTokenSecurityScheme());

        return new OpenAPI()
            .info(info())
            .components(components)
            .addSecurityItem(accessTokenRequirement)
            .addSecurityItem(refreshTokenRequirement);
    }

    private SecurityScheme accessTokenSecurityScheme() {
        return new SecurityScheme()
            .name(JwtUtil.ACCESS_TOKEN_HEADER) // Access-Token 을 키로 함
            .type(Type.APIKEY) // HTTP 가 아니므로, name 값을 키로 함. (HTTP 설정 시 Authorization 고정)
            .in(In.HEADER) // 액세스 토큰은 헤더에 속함
            .scheme(BEARER_TOKEN_PREFIX) // Bearer 접두사 붙음
            .bearerFormat("JWT"); // 유형은 JWT
    }

    private SecurityScheme refreshTokenSecurityScheme() {
        return new SecurityScheme()
            .name(JwtUtil.REFRESH_TOKEN_HEADER) // Refresh-Token 을 키로 함
            .type(Type.APIKEY) // name 값을 키로 하기 위해 설정
            .in(In.COOKIE); // 리프레쉬 토큰은 쿠키에 속함
    }

    private Info info() {
        return new Info()
            .title("인사이트")
            .description("인성면접, 사람들의 경험으로 잇다.")
            .version("1.0.0");
    }
}
