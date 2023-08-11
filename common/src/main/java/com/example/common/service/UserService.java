package com.example.common.service;


import com.example.common.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    User create(User contact);
    User readById(long id);
    User update(User contact);
    void delete(User contact);
    List<User> getAll();
    User readByEmail(String contact);
    UserDetailsService userDetailsService();

}
