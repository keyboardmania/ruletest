package com.ruletest.todo.controller;

import com.ruletest.todo.request.CreateTodoRequestDto;
import com.ruletest.todo.request.DeleteTodoRequestDto;
import com.ruletest.todo.request.FindTodoRequestDto;
import com.ruletest.todo.request.UpdateTodoRequestDto;
import com.ruletest.todo.response.CommonResponseDto;
import com.ruletest.todo.response.TodoResponseDto;
import com.ruletest.todo.service.TodoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDto<TodoResponseDto>> createTodo(
            @Valid @RequestBody CreateTodoRequestDto requestDto
    ) {
        TodoResponseDto responseDto = todoService.createTodo(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponseDto.success(HttpStatus.CREATED.value(), "할 일을 등록하다.", responseDto));
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto<List<TodoResponseDto>>> findTodos() {
        List<TodoResponseDto> responseDtos = todoService.findTodos();
        return ResponseEntity.ok(CommonResponseDto.success(HttpStatus.OK.value(), "할 일 목록을 조회하다.", responseDtos));
    }

    @GetMapping("/detail")
    public ResponseEntity<CommonResponseDto<TodoResponseDto>> findTodo(
            @Valid @RequestBody FindTodoRequestDto requestDto
    ) {
        TodoResponseDto responseDto = todoService.findTodo(requestDto);
        return ResponseEntity.ok(CommonResponseDto.success(HttpStatus.OK.value(), "할 일을 조회하다.", responseDto));
    }

    @PutMapping
    public ResponseEntity<CommonResponseDto<TodoResponseDto>> updateTodo(
            @Valid @RequestBody UpdateTodoRequestDto requestDto
    ) {
        TodoResponseDto responseDto = todoService.updateTodo(requestDto);
        return ResponseEntity.ok(CommonResponseDto.success(HttpStatus.OK.value(), "할 일을 수정하다.", responseDto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTodo(@Valid @RequestBody DeleteTodoRequestDto requestDto) {
        todoService.deleteTodo(requestDto);
        return ResponseEntity.noContent().build();
    }
}
