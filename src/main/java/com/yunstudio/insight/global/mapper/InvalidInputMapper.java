package com.yunstudio.insight.global.mapper;

import com.yunstudio.insight.global.exception.InvalidInputRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.validation.FieldError;

@Mapper
public interface InvalidInputMapper {

    InvalidInputMapper INSTANCE = Mappers.getMapper(InvalidInputMapper.class);

    @Mapping(source = "defaultMessage", target = "message")
    InvalidInputRes toInvalidInputRes(FieldError fieldError);
}
