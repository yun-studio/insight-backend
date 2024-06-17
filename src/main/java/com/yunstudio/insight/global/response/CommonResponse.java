package com.yunstudio.insight.global.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {

    private HttpStatus status;
    private Integer code;
    private String message;
    private T data;

    /**
     * data 필드에 값을 넣을 때 사용하는 메서드 - data 필드가 필요 없는 경우
     */
    public static CommonResponse<Object> success() {
        return CommonResponse.builder()
            .status(ResultCase.SUCCESS.getHttpStatus())
            .code(ResultCase.SUCCESS.getCode())
            .message(ResultCase.SUCCESS.getMessage())
            .data(new EmptyResponseDto())
            .build();
    }

    /**
     * data 필드에 값을 넣을 때 사용하는 메서드 - data 필드가 필요한 경우
     */
    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
            .status(ResultCase.SUCCESS.getHttpStatus())
            .code(ResultCase.SUCCESS.getCode())
            .message(ResultCase.SUCCESS.getMessage())
            .data(data)
            .build();
    }

    /**
     * 에러 발생 시 특정 에러에 맞는 응답하는 메서드 - data 필드가 필요 없는 경우
     */
    public static ResponseEntity<CommonResponse<Object>> error(ResultCase resultCase) {
        CommonResponse<Object> response = CommonResponse.builder()
            .status(resultCase.getHttpStatus())
            .code(resultCase.getCode())
            .message(resultCase.getMessage())
            .data(new EmptyResponseDto())
            .build();

        return ResponseEntity
            .status(resultCase.getHttpStatus())
            .body(response);
    }

    /**
     * 에러 발생 시 특정 에러에 맞는 응답하는 메서드 - data 필드가 필요한 경우
     */
    public static <T> ResponseEntity<CommonResponse<T>> error(ResultCase resultCase, T data) {
        CommonResponse<T> response = CommonResponse.<T>builder()
            .status(resultCase.getHttpStatus())
            .code(resultCase.getCode())
            .message(resultCase.getMessage())
            .data(data)
            .build();

        return ResponseEntity
            .status(resultCase.getHttpStatus())
            .body(response);
    }

    @JsonIgnoreProperties
    private static class EmptyResponseDto {

    }
}
