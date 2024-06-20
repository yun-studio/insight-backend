package com.yunstudio.insight.domain.user.controller;

import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.domain.user.service.UserService;
import com.yunstudio.insight.global.response.CommonResponse;
import com.yunstudio.insight.global.security.LoginUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
}
