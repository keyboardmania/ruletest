package com.ruletest.todo.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // HTTP 400 (4000~4099)
    INVALID_INPUT_VALUE(400, 4000, "입력값이 올바르지 않다."),
    // HTTP 404 (4400~4499)
    TODO_NOT_FOUND(404, 4400, "할 일을 찾을 수 없다."),
    // HTTP 500 (5000~5099)
    INTERNAL_SERVER_ERROR(500, 5000, "서버 내부 오류가 발생하다.");

    private final int httpStatus;
    private final int code;
    private final String message;

    ErrorCode(int httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
