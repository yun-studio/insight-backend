package com.yunstudio.insight.domain.question.repository;

import com.yunstudio.insight.domain.question.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface QuestionRepositoryCustom {

    Slice<Question> findAllByQueryPaging(String content, Pageable pageable);
}
