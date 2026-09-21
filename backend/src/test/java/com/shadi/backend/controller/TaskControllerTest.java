package com.shadi.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.shadi.backend.model.User;
import com.shadi.backend.repository.TaskRepository;
import com.shadi.backend.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private User user;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User newUser = new User();
        newUser.setName("John Smith");
        newUser.setEmail("john@example.com");

        user = userRepository.save(newUser);
    }

    @Test
    void shouldCreateTask() throws Exception {

        String requestBody = """
            {
              "title": "Finish Spring Boot project",
              "description": "Complete the backend API",
              "status": "TODO",
              "priority": "HIGH",
              "dueDate": "2026-09-21",
              "user": {
                "id": %d
              }
            }
            """.formatted(user.getId());

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Finish Spring Boot project"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.user.id").value(user.getId()));
    }
    @Test
    void shouldReturn400WhenTitleIsBlank() throws Exception {

        String requestBody = """
            {
            "title": "   ",
            "description": "Testing validation",
            "status": "TODO",
            "priority": "HIGH",
            "dueDate": "2026-09-21",
            "user": {
                "id": %d
            }
            }
            """.formatted(user.getId());

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errors.title")
                        .value("Title is required!"));
    }

    @Test
    void shouldReturn404WhenUserDoesNotExist() throws Exception {

        String requestBody = """
            {
            "title": "Test nonexistent user",
            "description": "Testing missing user",
            "status": "TODO",
            "priority": "HIGH",
            "dueDate": "2026-09-21",
            "user": {
                "id": 9999
            }
            }
            """;

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("User not found with id: 9999"));
    }
    @Test
    void shouldReturn400WhenStatusIsInvalid() throws Exception {

        String requestBody = """
            {
            "title": "Invalid status test",
            "description": "Testing enum validation",
            "status": "WHATEVER",
            "priority": "HIGH",
            "dueDate": "2026-09-21",
            "user": {
                "id": %d
            }
            }
            """.formatted(user.getId());

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}