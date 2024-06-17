package com.yunstudio.insight.global.response;

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

    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
            .status(ResultCase.SUCCESS.getHttpStatus())
            .code(ResultCase.SUCCESS.getCode())
            .message(ResultCase.SUCCESS.getMessage())
            .data(data)
            .build();
    }

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
}
