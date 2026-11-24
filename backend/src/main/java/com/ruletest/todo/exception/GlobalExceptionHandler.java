package com.ruletest.todo.exception;

import com.ruletest.todo.response.CommonResponseDto;
import com.ruletest.todo.response.FieldErrorResponseDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseDto<List<FieldErrorResponseDto>>> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        List<FieldErrorResponseDto> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map((fieldError) -> new FieldErrorResponseDto(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .toList();
        return errorResponse(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponseDto<List<FieldErrorResponseDto>>> handleUnreadable(
            HttpMessageNotReadableException exception
    ) {
        return errorResponse(ErrorCode.INVALID_INPUT_VALUE, List.of());
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<CommonResponseDto<Void>> handleTodoNotFound(TodoNotFoundException exception) {
        return errorResponse(ErrorCode.TODO_NOT_FOUND, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseDto<Void>> handleException(Exception exception) {
        log.error("요청 처리 실패", exception);
        return errorResponse(ErrorCode.INTERNAL_SERVER_ERROR, null);
    }

    private <T> ResponseEntity<CommonResponseDto<T>> errorResponse(ErrorCode errorCode, T data) {
        CommonResponseDto<T> body = CommonResponseDto.fail(errorCode, data);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }
}
