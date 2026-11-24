package com.ruletest.todo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateTodoRequestDto(
        @NotNull(message = "할 일 번호는 필수다.")
        @Positive(message = "할 일 번호는 1 이상이다.")
        Long todoId,
        @NotBlank(message = "제목은 비울 수 없다.")
        @Size(max = 100, message = "제목은 100자를 넘길 수 없다.")
        String title,
        @NotNull(message = "완료 여부는 필수다.")
        Boolean completed
) {
}
