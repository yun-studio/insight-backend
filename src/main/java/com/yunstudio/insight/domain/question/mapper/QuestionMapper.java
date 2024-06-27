package com.yunstudio.insight.domain.question.mapper;

import com.yunstudio.insight.domain.answer.dto.response.GetAnswerRes;
import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.answer.mapper.AnswerMapper;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionRes;
import com.yunstudio.insight.domain.question.dto.response.GetQuestionsRes;
import com.yunstudio.insight.domain.question.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Slice;

@Mapper
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    default GetQuestionRes toGetQuestionRes(Question question, Slice<Answer> answerList) {
        Slice<GetAnswerRes> getAnswerResList = answerList
            .map(AnswerMapper.INSTANCE::toGetAnswerRes);

        return toGetQuestionResWithGetAnswerResList(question, getAnswerResList);
    }

    @Mapping(source = "question.content", target = "content")
    @Mapping(source = "answerList", target = "answerList")
    GetQuestionRes toGetQuestionResWithGetAnswerResList(Question question, Slice<GetAnswerRes> answerList);

    @Mapping(expression = "java(question.getAnswerList().size())", target = "answerCount")
    GetQuestionsRes toGetQuestionsRes(Question question);
}
