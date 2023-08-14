package com.example.common.controller;

import com.example.common.model.Role;
import com.example.common.request.UserRequest;
import com.example.common.service.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerTestClass {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void addAuthUser() throws Exception {
        Role role = new Role();
        role.setName("USER");
        roleService.create(role);

        UserRequest loginRequest = new UserRequest("Test","Test","test@gmail.com", "5b2h1k");
        String requestBody = asJsonString(loginRequest);

        mockMvc.perform(post("/ENS-Ukraine/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
