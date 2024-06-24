package com.yunstudio.insight.domain.question.mapper;

import com.yunstudio.insight.domain.question.dto.response.GetQuestionRes;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionsRes;
import com.yunstudio.insight.domain.question.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    GetQuestionRes toGetQuestionRes(Question question);

    @Mapping(expression = "java(question.getAnswerList().size())", target = "answerCount")
    GetQuestionsRes toGetQuestionsRes(Question question);
}
