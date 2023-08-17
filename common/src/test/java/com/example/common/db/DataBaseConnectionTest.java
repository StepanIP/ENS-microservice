package com.example.common.db;

import com.example.common.model.Contact;
import com.example.common.model.Role;
import com.example.common.model.User;
import com.example.common.repository.ContactRepository;
import com.example.common.repository.RoleRepository;
import com.example.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DataBaseConnectionTest {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    @Transactional
    void testCreateContact(){
        Role role = new Role();
        role.setName("TEST");
        roleRepository.save(role);
        User user = new User("Test","Test","test@gmail.com", "5b2h1k" ,roleRepository.findById(role.getId()).get());
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setContact("myGmail@gmail.com");
        contact.setOwner(user);

        int beforeSize = contactRepository.findAll().size();

        contactRepository.save(contact);
        Optional<Contact> actual = contactRepository.findUserByContact(contact.getContact());

        assertEquals(contact.getContact(), actual.get().getContact());
        assertNotEquals(beforeSize, contactRepository.findAll().size());
    }

}
