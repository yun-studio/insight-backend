package com.yunstudio.insight.global.exception;

import com.yunstudio.insight.global.mapper.InvalidInputMapper;
import com.yunstudio.insight.global.response.CommonResponse;
import com.yunstudio.insight.global.response.ResultCase;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Business 오류 발생에 대한 핸들러
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<CommonResponse<Object>> handleGlobalException(GlobalException e) {
        return CommonResponse.error(e.getResultCase());
    }

    /**
     * RequestBody 입력 파라미터 검증 오류 발생에 대한 핸들러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<List<InvalidInputRes>>> handlerValidationException(MethodArgumentNotValidException e) {
        List<InvalidInputRes> invalidInputResList = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(InvalidInputMapper.INSTANCE::toInvalidInputRes)
            .toList();

        return CommonResponse.error(ResultCase.INVALID_INPUT, invalidInputResList);
    }
}
