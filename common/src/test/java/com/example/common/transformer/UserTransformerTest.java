package com.example.common.transformer;

import com.example.common.model.Role;
import com.example.common.model.User;
import com.example.common.request.UserRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTransformerTest {

    @Test
    public void testTransformToEntity_ValidUserRequestAndRole_Success() {
        UserRequest userRequest = new UserRequest("John", "Doe", "john.doe@example.com", "password");
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        User user = UserTransformer.transformToEntity(userRequest, role);

        assertNotNull(user);
        assertEquals(userRequest.getName(), user.getName());
        assertEquals(userRequest.getSurname(), user.getSurname());
        assertEquals(userRequest.getEmail(), user.getEmail());
        assertEquals(userRequest.getPassword(), user.getPassword());
        assertEquals(role, user.getRole());
    }

    @Test
    public void testTransformToDto_ValidUser_Success() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        UserRequest userRequest = UserTransformer.transformToDto(user);

        assertNotNull(userRequest);
        assertEquals(user.getName(), userRequest.getName());
        assertEquals(user.getSurname(), userRequest.getSurname());
        assertEquals(user.getEmail(), userRequest.getEmail());
        assertEquals(user.getPassword(), userRequest.getPassword());
    }
}
