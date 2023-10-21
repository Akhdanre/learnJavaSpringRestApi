package com.learn.javaspring.belajarspringrestapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.javaspring.belajarspringrestapi.entity.User;
import com.learn.javaspring.belajarspringrestapi.model.LoginUserRequest;
import com.learn.javaspring.belajarspringrestapi.model.TokenResponse;
import com.learn.javaspring.belajarspringrestapi.model.WebResponse;
import com.learn.javaspring.belajarspringrestapi.repository.UserRepository;
import com.learn.javaspring.belajarspringrestapi.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void TestAuthLoginUnauthorized() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("ujicoba");
        request.setPassword("ujicoba");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }


    @Test
    void TestAuthLoginWrongPassword() throws Exception {
        User user = new User();
        user.setUsername("ouken");
        user.setPassword(BCrypt.hashpw("lorem12", BCrypt.gensalt()));
        user.setName("oukenzeuma");
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("ouken");
        request.setPassword("lorem11");

        mockMvc.perform(
                        post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void TestAuthLoginSuccess() throws Exception {
        User user = new User();
        user.setName("dani orland");
        user.setUsername("dani");
        user.setPassword(BCrypt.hashpw("passwordnya", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("dani");
        request.setPassword("passwordnya");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

            });
            assertNull( response.getErrors());
            assertNotNull(response.getData().getToken());

            User userDb = userRepository.findById("dani").orElse(null);
            assertNotNull(userDb);
            assertEquals(userDb.getToken(), response.getData().getToken());
        });
    }


}
