package com.yunstudio.insight.domain.answer.presentation;

import com.yunstudio.insight.domain.answer.business.AnswerCreateService;
import com.yunstudio.insight.domain.answer.business.AnswerDeleteService;
import com.yunstudio.insight.domain.answer.dto.request.CreateAnswerReq;
import com.yunstudio.insight.domain.answer.dto.response.CreateAnswerRes;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.CommonResponse;
import com.yunstudio.insight.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerCreateService answerCreateService;
    private final AnswerDeleteService answerDeleteService;

    /**
     * 답변 생성.
     */
    @PostMapping
    public CommonResponse<CreateAnswerRes> createAnswer(
        @LoginUser User user,
        @PathVariable Long questionId,
        @RequestBody CreateAnswerReq request
    ) {

        CreateAnswerRes response = answerCreateService.createAnswer(user, questionId, request);

        return CommonResponse.success(response);
    }

    /**
     * 답변 삭제.
     */
    @DeleteMapping("/{id}")
    public CommonResponse<CommonEmptyRes> deleteAnswer(
        @LoginUser User user,
        @PathVariable Long questionId,
        @PathVariable Long id
    ) {

        CommonEmptyRes response = answerDeleteService.deleteAnswer(user, questionId, id);

        return CommonResponse.success(response);
    }
}
