package com.yunstudio.insight.domain.question.repository;

import com.yunstudio.insight.domain.question.dto.response.GetQuestionRes;
import java.util.List;

public interface QuestionRepositoryCustom {

    List<GetQuestionRes> findAllByQuery(String query);
}
