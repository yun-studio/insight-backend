package com.yunstudio.insight.global.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunstudio.insight.global.response.CommonResponse;
import com.yunstudio.insight.global.response.ResultCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j(topic = "logout success handler")
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String json = createResponseJson(response);
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("logout response to json error", e);
        }
    }

    private String createResponseJson(HttpServletResponse response) throws JsonProcessingException {
        response.setStatus(ResultCase.SUCCESS.getHttpStatus().value()); // http status 200
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Content-Type : application/json
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // charset : utf8

        return new ObjectMapper().writeValueAsString(CommonResponse.success());
    }
}
