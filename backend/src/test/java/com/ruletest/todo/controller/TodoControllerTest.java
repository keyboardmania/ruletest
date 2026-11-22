package com.ruletest.todo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "DB_USERNAME=sa",
        "DB_PASSWORD=test",
})
@Transactional
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    void createTodoReturnsCreatedTodo() throws Exception {
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"  우유 사기  \"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("할 일을 등록하다."))
                .andExpect(jsonPath("$.data.title").value("우유 사기"))
                .andExpect(jsonPath("$.data.completed").value(false))
                .andExpect(jsonPath("$.data.todoId").isNumber())
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.respondedAt").exists());
    }

    @Test
    void createTodoRejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"   \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(4000))
                .andExpect(jsonPath("$.message").value("입력값이 올바르지 않다."))
                .andExpect(jsonPath("$.data[0].field").value("title"))
                .andExpect(jsonPath("$.data[0].message").value("제목은 비울 수 없다."));
    }

    @Test
    void findTodosReturnsCreatedTodos() throws Exception {
        createTodo("첫 번째");
        createTodo("두 번째");

        mockMvc.perform(get("/api/v1/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("할 일 목록을 조회하다."))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].title").value("첫 번째"))
                .andExpect(jsonPath("$.data[1].title").value("두 번째"));
    }

    @Test
    void findTodoReturnsOneTodo() throws Exception {
        long todoId = createTodo("조회 대상");

        mockMvc.perform(get("/api/v1/todos/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"todoId\":" + todoId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("할 일을 조회하다."))
                .andExpect(jsonPath("$.data.todoId").value(todoId))
                .andExpect(jsonPath("$.data.title").value("조회 대상"));
    }

    @Test
    void findTodoReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/todos/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"todoId\":99999}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(4400))
                .andExpect(jsonPath("$.message").value("할 일을 찾을 수 없다."));
    }

    @Test
    void updateTodoChangesTitleAndCompleted() throws Exception {
        long todoId = createTodo("수정 전");

        mockMvc.perform(put("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"todoId\":" + todoId + ",\"title\":\"수정 후\",\"completed\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("할 일을 수정하다."))
                .andExpect(jsonPath("$.data.title").value("수정 후"))
                .andExpect(jsonPath("$.data.completed").value(true));
    }

    @Test
    void updateTodoRejectsMissingCompleted() throws Exception {
        long todoId = createTodo("수정 검증");

        mockMvc.perform(put("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"todoId\":" + todoId + ",\"title\":\"수정\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(4000))
                .andExpect(jsonPath("$.data[0].field").value("completed"));
    }

    @Test
    void deleteTodoReturnsNoContent() throws Exception {
        long todoId = createTodo("삭제 대상");

        mockMvc.perform(delete("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"todoId\":" + todoId + "}"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        mockMvc.perform(get("/api/v1/todos/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"todoId\":" + todoId + "}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void storesColumnsInSnakeCase() throws Exception {
        createTodo("스키마");
        entityManager.flush();

        Number count = (Number) entityManager.createNativeQuery("select count(*) from todo").getSingleResult();
        assertThat(count.intValue()).isEqualTo(1);

        @SuppressWarnings("unchecked")
        List<String> columnNames = entityManager.createNativeQuery(
                "select lower(column_name) from information_schema.columns where lower(table_name) = 'todo'"
        ).getResultList();
        assertThat(columnNames).contains("todo_id", "title", "completed", "created_at");
    }

    private long createTodo(String title) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + title + "\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode body = jsonMapper.readTree(result.getResponse().getContentAsString());
        return body.path("data").path("todoId").asLong();
    }
}
