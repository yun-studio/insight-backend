package com.yunstudio.insight.domain.question.controller;

import com.yunstudio.insight.domain.question.dto.request.CreateQuestionReq;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionRes;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionsRes;
import com.yunstudio.insight.domain.question.service.QuestionService;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.CommonResponse;
import com.yunstudio.insight.global.security.LoginUser;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public CommonResponse<List<GetQuestionsRes>> getQuestions(
        @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {

        List<GetQuestionsRes> response = questionService.getQuestions(query);

        return CommonResponse.success(response);
    }

    @GetMapping("/{id}")
    public CommonResponse<GetQuestionRes> getQuestion(@PathVariable Long id) {
        GetQuestionRes response = questionService.getQuestion(id);

        return CommonResponse.success(response);
    }

    @PostMapping
    public CommonResponse<CommonEmptyRes> createQuestion(@LoginUser User user, @Valid @RequestBody CreateQuestionReq request) {
        CommonEmptyRes response = questionService.createQuestion(user.getId(), request);

        return CommonResponse.success(response);
    }
}
