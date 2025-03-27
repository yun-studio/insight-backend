package com.yunstudio.insight.domain.answer.mapper;

import com.yunstudio.insight.domain.answer.dto.response.CreateAnswerRes;
import com.yunstudio.insight.domain.answer.dto.response.GetAnswerRes;
import com.yunstudio.insight.domain.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    @Mapping(expression = "java(answer.getLikeList().size())", target = "likeCount")
    GetAnswerRes toGetAnswerRes(Answer answer);

    @Mapping(target = "likeCount", constant = "0")
    CreateAnswerRes toCreateAnswerRes(Answer answer);
}
