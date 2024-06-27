package com.yunstudio.insight.domain.answer.repository;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.user.dto.response.UserAnswerRes;
import com.yunstudio.insight.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AnswerRepositoryCustom {

    List<UserAnswerRes> findAllUserLikeAnswers(User user);

    List<UserAnswerRes> findAllByAuthor(User author);

    Slice<Answer> findAllByQuestionPaging(Long questionId, Pageable pageable);
}
