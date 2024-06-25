package com.yunstudio.insight.domain.answer.controller;

import com.yunstudio.insight.domain.answer.service.AnswerService;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.CommonResponse;
import com.yunstudio.insight.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;

    @DeleteMapping("/{id}")
    public CommonResponse<CommonEmptyRes> deleteAnswer(@LoginUser User user, @PathVariable Long id) {

        CommonEmptyRes response = answerService.deleteAnswer(user, id);

        return CommonResponse.success(response);
    }
}
