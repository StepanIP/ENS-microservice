package com.example.common.service;

import com.example.common.model.Role;

import java.util.List;

public interface RoleService {
    Role create(Role role);
    Role readById(long id);
    Role update(Role role);
    void delete(long id);
    List<Role> getAll();
    Role readByName(String name);
}
