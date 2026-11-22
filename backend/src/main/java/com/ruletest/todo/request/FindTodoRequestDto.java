package com.ruletest.todo.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FindTodoRequestDto(
        @NotNull(message = "할 일 번호는 필수다.")
        @Positive(message = "할 일 번호는 1 이상이다.")
        Long todoId
) {
}
