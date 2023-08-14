package com.example.common.service;

import com.example.common.exception.NullEntityReferenceException;
import com.example.common.model.Contact;
import com.example.common.model.Role;
import com.example.common.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class ContactServiceTest {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    User user;

    @BeforeEach
    public void prepareUser(){
        Role role = new Role();
        role.setName("USER");
        roleService.create(role);

        user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("5b2h1k");
        user.setRole(role);

        userService.create(user);
    }

    @Test
    public void createNotificationTest(){
        Contact contact = new Contact();
        contact.setContact("test@gmail.com");
        contact.setOwner(userService.readById(user.getId()));

        List<Contact> before = contactService.getAll();

        Contact contact1 = contactService.create(contact);

        assertEquals(before.size()+1, contactService.getAll().size());
        assertEquals(contact, contactService.readByContact(contact.getContact()));
    }

    @Test
    public void createNotificationExceptionTest(){
        assertThrows(NullEntityReferenceException.class, () -> contactService.create(null));
    }

    @Test
    public void readByIdNotificationTest(){
        Contact contact = new Contact();
        contact.setOwner(userService.readById(user.getId()));
        contact.setContact("test@gmail.com");

        Contact contact1 = contactService.create(contact);

        assertEquals(contact, contactService.readById(contact.getId()));
    }

    @Test
    public void readByIdNotificationExceptionTest(){
        Contact contact = new Contact();
        contact.setOwner(userService.readById(user.getId()));
        contact.setContact("test@gmail.com");

        Contact contact1 = contactService.create(contact);

        assertThrows(EntityNotFoundException.class, () -> contactService.readById(contact.getId() + 1));
    }

    @Test
    public void updateNotificationTest(){
        Contact contact = new Contact();
        contact.setOwner(userService.readById(user.getId()));
        contact.setContact("test@gmail.com");

        Contact contact1 = contactService.create(contact);

        Contact updateContact = contactService.readById(contact.getId());
        updateContact.setOwner(userService.readById(user.getId()));
        updateContact.setContact("new@gmail.com");

        contactService.update(updateContact);

        assertEquals(updateContact, contactService.readById(updateContact.getId()));
    }

    @Test
    public void updateNotificationExceptionTest(){
        assertThrows(NullEntityReferenceException.class, () -> contactService.update(null));
    }

    @Test
    public void deleteNotificationTest(){
        Contact contact = new Contact();
        contact.setOwner(userService.readById(user.getId()));
        contact.setContact("test@gmail.com");

        contactService.create(contact);
        List<Contact> before = contactService.getAll();

        contactService.delete(contact);

        assertEquals(before.size()-1, contactService.getAll().size());
        assertThrows(EntityNotFoundException.class, () -> contactService.readByContact(contact.getContact()));
    }

    @Test
    public void getAllNotificationsTest(){
        List<Contact> expected = new ArrayList<>();
        Contact contact = new Contact();
        contact.setOwner(userService.readById(user.getId()));
        contact.setContact("test@gmail.com");
        expected.add(contact);

        Contact contact1 = new Contact();
        contact1.setOwner(userService.readById(user.getId()));
        contact1.setContact("testtwo@gmail.com");
        expected.add(contact1);

        contactService.create(contact);
        contactService.create(contact1);

        assertEquals(expected.size(), contactService.getAll().size());
        assertEquals(expected, contactService.getAll());
    }

    @Test
    public void getAllEmptyNotificationsTest(){
        assertEquals(new ArrayList<>(), contactService.getAll());
    }

    @Test
    public void readByNameNotificationTest(){
        Contact contact = new Contact();
        contact.setOwner(userService.readById(user.getId()));
        contact.setContact("test@gmail.com");

        contactService.create(contact);

        assertEquals(contact, contactService.readByContact(contact.getContact()));
    }

    @Test
    public void readByNameNotificationExceptionTest(){
        Contact contact = new Contact();
        contact.setOwner(userService.readById(user.getId()));
        contact.setContact("test@gmail.com");

        contactService.create(contact);

        assertThrows(EntityNotFoundException.class, () -> contactService.readByContact(contact.getContact() + 1));
    }
}
