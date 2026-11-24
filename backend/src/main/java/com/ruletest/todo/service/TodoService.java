package com.ruletest.todo.service;

import com.ruletest.todo.domain.Todo;
import com.ruletest.todo.exception.TodoNotFoundException;
import com.ruletest.todo.repository.TodoRepository;
import com.ruletest.todo.request.CreateTodoRequestDto;
import com.ruletest.todo.request.DeleteTodoRequestDto;
import com.ruletest.todo.request.FindTodoRequestDto;
import com.ruletest.todo.request.UpdateTodoRequestDto;
import com.ruletest.todo.response.TodoResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoResponseDto createTodo(CreateTodoRequestDto requestDto) {
        Todo todo = new Todo(requestDto.title().trim(), LocalDateTime.now());
        Todo savedTodo = todoRepository.save(todo);
        log.info("할 일 등록 완료. todoId={}", savedTodo.getTodoId());
        return TodoResponseDto.from(savedTodo);
    }

    @Transactional(readOnly = true)
    public List<TodoResponseDto> findTodos() {
        return todoRepository.findAll(Sort.by(Sort.Direction.ASC, "todoId"))
                .stream()
                .map(TodoResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TodoResponseDto findTodo(FindTodoRequestDto requestDto) {
        return TodoResponseDto.from(getTodo(requestDto.todoId()));
    }

    public TodoResponseDto updateTodo(UpdateTodoRequestDto requestDto) {
        Todo todo = getTodo(requestDto.todoId());
        todo.updateTodo(requestDto.title().trim(), requestDto.completed());
        log.info("할 일 수정 완료. todoId={}", todo.getTodoId());
        return TodoResponseDto.from(todo);
    }

    public void deleteTodo(DeleteTodoRequestDto requestDto) {
        Todo todo = getTodo(requestDto.todoId());
        todoRepository.delete(todo);
        log.info("할 일 삭제 완료. todoId={}", requestDto.todoId());
    }

    private Todo getTodo(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(TodoNotFoundException::new);
    }
}
