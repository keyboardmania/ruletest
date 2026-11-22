package com.ruletest.todo.response;

import com.ruletest.todo.domain.Todo;
import java.time.LocalDateTime;

public record TodoResponseDto(
        Long todoId,
        String title,
        boolean completed,
        LocalDateTime createdAt
) {

    public static TodoResponseDto from(Todo todo) {
        return new TodoResponseDto(
                todo.getTodoId(),
                todo.getTitle(),
                todo.isCompleted(),
                todo.getCreatedAt()
        );
    }
}
