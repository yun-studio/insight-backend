package com.yunstudio.insight.domain.user.controller;

import com.yunstudio.insight.domain.user.dto.request.UserUpdateNicknameReq;
import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.domain.user.entity.UserRole;
import com.yunstudio.insight.domain.user.service.UserService;
import com.yunstudio.insight.global.jwt.JwtUtil;
import com.yunstudio.insight.global.redis.RedisUtil;
import com.yunstudio.insight.global.response.CommonResponse;
import com.yunstudio.insight.global.security.LoginUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/answers")
    public CommonResponse<List<UserAnswerRes>> getMyAnswers(@LoginUser User user) {

        List<UserAnswerRes> userAnswerResList = userService.getMyAnswers(user);

        return CommonResponse.success(userAnswerResList);
    }

    @GetMapping("/likes")
    public CommonResponse<List<UserAnswerRes>> getLikeAnswers(@LoginUser User user) {

        List<UserAnswerRes> userAnswerResList = userService.getLikeAnswers(user);

        return CommonResponse.success(userAnswerResList);
    }

    @PatchMapping("/nickname")
    public CommonResponse<Object> changeNickname(
        @LoginUser User user,
        @RequestBody UserUpdateNicknameReq request,
        HttpServletResponse response
    ) {

        userService.changeNickname(user, request.nickname(), response);

        return CommonResponse.success();
    }

    @DeleteMapping()
    public CommonResponse<Object> deleteUser(@LoginUser User user, HttpServletResponse response) {

        userService.deleteUser(user, response);

        return CommonResponse.success();
    }
}
