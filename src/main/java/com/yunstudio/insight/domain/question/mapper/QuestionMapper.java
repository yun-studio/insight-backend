package com.yunstudio.insight.domain.question.mapper;

import com.yunstudio.insight.domain.answer.dto.response.GetAnswerRes;
import com.yunstudio.insight.domain.answer.mapper.AnswerMapper;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionRes;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionsRes;
import com.yunstudio.insight.domain.question.entity.Question;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    default GetQuestionRes toGetQuestionRes(Question question) {
        List<GetAnswerRes> getAnswerResList = question.getAnswerList()
            .stream()
            .map(AnswerMapper.INSTANCE::toGetAnswerRes)
            .toList();

        return toGetQuestionResWithGetAnswerResList(question, getAnswerResList);
    }

    @Mapping(source = "answerList", target = "answerList")
    GetQuestionRes toGetQuestionResWithGetAnswerResList(Question question, List<GetAnswerRes> answerList);

    @Mapping(expression = "java(question.getAnswerList().size())", target = "answerCount")
    GetQuestionsRes toGetQuestionsRes(Question question);
}
