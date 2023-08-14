package com.example.common.service.impl;

import com.example.common.exception.NullEntityReferenceException;
import com.example.common.model.User;
import com.example.common.repository.UserRepository;
import com.example.common.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User create(User user) {
        LOGGER.info("Creating user: {}", user);
        if (user != null) {
            User savedUser = userRepository.save(user);
            LOGGER.info("User created successfully.");
            return savedUser;
        }
        LOGGER.error("User cannot be 'null'");
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public User readById(long id) {
        LOGGER.info("Fetching user by ID: {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> {
                    LOGGER.error("User with ID {} not found", id);
                    return new EntityNotFoundException("User with id " + id + " not found");
                }
        );
    }

    @Override
    public User update(User user) {
        LOGGER.info("Updating user: {}", user);
        if (user != null) {
            readById(user.getId());
            User updatedUser = userRepository.save(user);
            LOGGER.info("User updated successfully.");
            return updatedUser;
        }
        LOGGER.error("User cannot be 'null'");
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public void delete(User user) {
        LOGGER.info("Deleting user: {}", user);
        userRepository.delete(user);
        LOGGER.info("User deleted successfully.");
    }

    @Override
    public List<User> getAll() {
        LOGGER.info("Fetching all users.");
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            LOGGER.info("No users found.");
            return new ArrayList<>();
        }
        LOGGER.info("Fetched {} users.", users.size());
        return users;
    }

    @Override
    public User readByEmail(String email) {
        LOGGER.info("Fetching user by username: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            LOGGER.error("User with username {} not found", email);
            throw new EntityNotFoundException("User with username " + email + " not found");
        }
        return user;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username);
            }
        };
    }

}
