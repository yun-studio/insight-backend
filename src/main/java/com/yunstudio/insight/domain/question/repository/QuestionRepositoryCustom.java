package com.yunstudio.insight.domain.question.repository;

import com.yunstudio.insight.domain.question.dto.response.GetQuestionsRes;
import java.util.List;

public interface QuestionRepositoryCustom {

    List<GetQuestionsRes> findAllByQuery(String query);
}
