package com.yunstudio.insight.domain.answer.repository;

import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.entity.User;
import java.util.List;

public interface AnswerRepositoryCustom {

    List<UserAnswerRes> findAllUserLikeAnswers(User user);

    List<UserAnswerRes> findAllByAuthor(User author);
}
