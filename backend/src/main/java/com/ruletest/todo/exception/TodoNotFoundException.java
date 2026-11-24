package com.ruletest.todo.exception;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException() {
        super(ErrorCode.TODO_NOT_FOUND.getMessage());
    }
}
