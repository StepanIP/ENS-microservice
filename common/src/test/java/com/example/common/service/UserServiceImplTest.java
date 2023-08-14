package com.example.common.service;

import com.example.common.exception.NullEntityReferenceException;
import com.example.common.model.User;
import com.example.common.repository.UserRepository;
import com.example.common.service.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateUser_ValidUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);
        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getSurname(), createdUser.getSurname());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    public void testCreateUser_NullUser_ThrowsNullEntityReferenceException() {
        UserServiceImpl userService = new UserServiceImpl();

        assertThrows(NullEntityReferenceException.class, () -> userService.create(null));
    }


    @Test
    public void testReadUserById_ExistingUserId_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.readById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getSurname(), foundUser.getSurname());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    public void testReadUserById_NonExistingUserId_ExceptionThrown() {
        long nonExistingId = 999;

        assertThrows(EntityNotFoundException.class, () -> userService.readById(nonExistingId));
    }

    @Test
    public void testUpdateUser_ValidUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updatedUser = userService.update(user);

        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getName(), updatedUser.getName());
        assertEquals(user.getSurname(), updatedUser.getSurname());
        assertEquals(user.getEmail(), updatedUser.getEmail());
    }

    @Test
    public void testUpdateUser_NullUser_ExceptionThrown() {
        assertThrows(NullEntityReferenceException.class, () -> userService.update(null));
    }

    @Test
    public void testUpdateUser_NonExistingUserId_ExceptionThrown() {
        User user = new User();
        user.setId(999L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");

        assertThrows(EntityNotFoundException.class, () -> {
            when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
            userService.update(user);
        });
    }


    @Test
    public void testDeleteUser_ValidUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");

        userService.delete(user);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testGetAllUsers_NoUsersInDatabase_ReturnsEmptyList() {
        List<User> emptyList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(emptyList);
        List<User> users = userService.getAll();

        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    public void testGetAllUsers_UsersInDatabase_ReturnsListOfUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John");
        user1.setSurname("Doe");
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane");
        user2.setSurname("Smith");
        user2.setEmail("jane.smith@example.com");

        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);
        List<User> users = userService.getAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(user1.getId(), users.get(0).getId());
        assertEquals(user1.getName(), users.get(0).getName());
        assertEquals(user2.getSurname(), users.get(1).getSurname());
        assertEquals(user2.getEmail(), users.get(1).getEmail());
    }

    @Test
    public void testReadUserByEmail_ExistingEmail_Success() {
        String email = "john.doe@example.com";
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);
        User foundUser = userService.readByEmail(email);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getSurname(), foundUser.getSurname());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    public void testReadUserByEmail_NonExistingEmail_ExceptionThrown() {
        String nonExistingEmail = "nonexisting@example.com";

        assertThrows(EntityNotFoundException.class, () -> {
            when(userRepository.findByEmail(nonExistingEmail)).thenReturn(null);
            userService.readByEmail(nonExistingEmail);
        });
    }
}
