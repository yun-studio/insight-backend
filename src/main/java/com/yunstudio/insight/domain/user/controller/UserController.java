package com.yunstudio.insight.domain.user.controller;

import com.yunstudio.insight.domain.user.dto.request.UserSignupReq;
import com.yunstudio.insight.domain.user.dto.request.UserUpdateNicknameReq;
import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.dto.response.UserChangeNicknameRes;
import com.yunstudio.insight.domain.user.dto.response.UserDeleteRes;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.domain.user.entity.UserRole;
import com.yunstudio.insight.domain.user.service.UserService;
import com.yunstudio.insight.global.jwt.JwtUtil;
import com.yunstudio.insight.global.redis.RedisUtil;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.CommonResponse;
import com.yunstudio.insight.global.security.LoginUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserService userService;

    @GetMapping()
    public User getMe(@LoginUser User user) {
        return user;
    }

    @GetMapping("/dummy")
    public CommonResponse<CommonEmptyRes> dummy(HttpServletResponse response) {
        String refreshToken = jwtUtil.createRefreshToken("monkey", UserRole.USER.getAuthority());
        String accessToken = jwtUtil.setTokenWithBearer(jwtUtil.createAccessToken("monkey", UserRole.USER.getAuthority()));

        Cookie cookie = jwtUtil.createRefreshTokenCookie(refreshToken);

        response.addCookie(cookie);
        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, accessToken);

        redisUtil.setUserLogin("monkey", refreshToken);

        return CommonResponse.success();
    }

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

    @PostMapping("/signup")
    public CommonResponse<CommonEmptyRes> signup(@RequestBody UserSignupReq request) {

        CommonEmptyRes response = userService.signup(request);
        
        return CommonResponse.success(response);
    }

    @PatchMapping("/nickname")
    public CommonResponse<UserChangeNicknameRes> changeNickname(@LoginUser User user, @Valid @RequestBody UserUpdateNicknameReq request) {

        UserChangeNicknameRes response = userService.changeNickname(user, request.nickname());

        return CommonResponse.success(response);
    }

    @DeleteMapping()
    public CommonResponse<UserDeleteRes> deleteUser(@LoginUser User user) {

        UserDeleteRes response = userService.deleteUser(user);

        return CommonResponse.success(response);
    }
}
