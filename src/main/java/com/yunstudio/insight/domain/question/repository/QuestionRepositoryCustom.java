package com.yunstudio.insight.domain.question.repository;

import com.yunstudio.insight.domain.question.entity.Question;
import java.util.List;

public interface QuestionRepositoryCustom {

    List<Question> findAllByQuery(String query);
}
