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

import com.shadi.backend.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws Exception {

        String requestBody = """
            {
              "name": "John Smith",
              "email": "john@example.com"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {

        String requestBody = """
            {
            "name": "John Smith",
            "email": "john@example.com"
            }
            """;

        // First request creates the user
        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated());

        // Second request uses the same email
        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                .value("User Email already exists:john@example.com"));
    }
}