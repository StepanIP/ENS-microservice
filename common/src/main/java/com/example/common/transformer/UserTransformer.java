package com.example.common.transformer;

import com.example.common.request.UserRequest;
import com.example.common.model.Role;
import com.example.common.model.User;

public class UserTransformer {

    public static User transformToEntity(UserRequest userRequest, Role role){
        User user = new User();
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setRole(role);
        return user;
    }

    public static UserRequest transformToDto(User user){
        return new UserRequest(
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
