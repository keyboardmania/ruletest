package com.ruletest.todo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    private String title;

    private boolean completed;

    private LocalDateTime createdAt;

    protected Todo() {
    }

    public Todo(String title, LocalDateTime createdAt) {
        this.title = title;
        this.completed = false;
        this.createdAt = createdAt;
    }

    public void updateTodo(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    public Long getTodoId() {
        return todoId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
