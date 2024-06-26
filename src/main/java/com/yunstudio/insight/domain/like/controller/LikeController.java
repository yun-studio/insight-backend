package com.yunstudio.insight.domain.like.controller;

import com.yunstudio.insight.domain.like.service.LikeService;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.CommonResponse;
import com.yunstudio.insight.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/answers/{answerId}/like")
public class LikeController {

    private final LikeService likeService;

    /**
     * 답변 좋아요
     */
    @PostMapping
    public CommonResponse<CommonEmptyRes> likeUp(@LoginUser User user, @PathVariable Long answerId) {

        CommonEmptyRes response = likeService.likeUp(user, answerId);

        return CommonResponse.success(response);
    }

    /**
     * 답변 좋아요 취소
     */
    @DeleteMapping
    public CommonResponse<CommonEmptyRes> dislike(@LoginUser User user, @PathVariable Long answerId) {

        CommonEmptyRes response = likeService.dislike(user, answerId);

        return CommonResponse.success(response);
    }
}
