package com.ruletest.todo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTodoRequestDto(
        @NotBlank(message = "제목은 비울 수 없다.")
        @Size(max = 100, message = "제목은 100자를 넘길 수 없다.")
        String title
) {
}
