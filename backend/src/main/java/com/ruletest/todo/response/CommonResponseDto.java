package com.ruletest.todo.response;

import com.ruletest.todo.exception.ErrorCode;
import java.time.LocalDateTime;

public record CommonResponseDto<T>(
        boolean success,
        LocalDateTime respondedAt,
        int code,
        String message,
        T data
) {

    public static <T> CommonResponseDto<T> success(int code, String message, T data) {
        return new CommonResponseDto<>(true, LocalDateTime.now(), code, message, data);
    }

    public static <T> CommonResponseDto<T> fail(ErrorCode errorCode, T data) {
        return new CommonResponseDto<>(
                false,
                LocalDateTime.now(),
                errorCode.getCode(),
                errorCode.getMessage(),
                data
        );
    }
}
