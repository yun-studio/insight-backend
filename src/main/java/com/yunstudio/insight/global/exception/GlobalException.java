package com.yunstudio.insight.global.exception;

import com.yunstudio.insight.global.response.ResultCase;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final ResultCase resultCase;

    public GlobalException(ResultCase resultCase) {
        super(resultCase.getMessage());
        this.resultCase = resultCase;
    }
}