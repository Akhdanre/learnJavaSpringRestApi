package com.learn.javaspring.belajarspringrestapi.controller;

import static org.junit.jupiter.api.Assertions.*;
// import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.javaspring.belajarspringrestapi.entity.User;
import com.learn.javaspring.belajarspringrestapi.model.RegisterUserRequest;
import com.learn.javaspring.belajarspringrestapi.model.WebResponse;
import com.learn.javaspring.belajarspringrestapi.repository.UserRepository;
import com.learn.javaspring.belajarspringrestapi.security.BCrypt;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSucces() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("p");
        request.setName("test");

        mockMvc.perform(
                post("/api/users").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {

                            });
                    assertEquals("OK", response.getData());
                });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");
        mockMvc.perform(
                post("/api/users").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testRegisterDuplicat() throws Exception {
        User user = new User();
        user.setUsername("akeon");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("akeon");
        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("akeon");
        request.setPassword("rahasia");
        request.setName("akeon");
        mockMvc.perform(
                post("/api/users").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });
                    assertNotNull(response.getErrors());
                });
    }


    @Test
    void getUserUnauthorized() throws Exception{
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "not found")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
            assertNotNull(response.getErrors());
        });
    }
}
