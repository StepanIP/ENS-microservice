package com.example.common.service.impl;

import com.example.common.exception.NullEntityReferenceException;
import com.example.common.model.Contact;
import com.example.common.repository.ContactRepository;
import com.example.common.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    private ContactRepository userRepository;

    @Override
    public Contact create(Contact contact) {
        LOGGER.info("Creating contact: {}", contact);
        if (contact != null) {
            Contact savedContact = userRepository.save(contact);
            LOGGER.info("Contact created successfully.");
            return savedContact;
        }
        LOGGER.error("Contact cannot be 'null'");
        throw new NullEntityReferenceException("Contact cannot be 'null'");
    }

    @Override
    public Contact readById(long id) {
        LOGGER.info("Fetching user by ID: {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> {
                    LOGGER.error("Contact with ID {} not found", id);
                    return new EntityNotFoundException("Contact with id " + id + " not found");
                }
        );
    }

    @Override
    public Contact update(Contact contact) {
        LOGGER.info("Updating contact: {}", contact);
        if (contact != null) {
            readById(contact.getId());
            Contact updatedContact = userRepository.save(contact);
            LOGGER.info("Contact updated successfully.");
            return updatedContact;
        }
        LOGGER.error("Contact cannot be 'null'");
        throw new NullEntityReferenceException("Contact cannot be 'null'");
    }

    @Override
    public void delete(Contact contact) {
        LOGGER.info("Deleting contact: {}", contact);
        userRepository.delete(contact);
        LOGGER.info("Contact deleted successfully.");
    }

    @Override
    public List<Contact> getAll() {
        LOGGER.info("Fetching all contacts.");
        List<Contact> contacts = userRepository.findAll();
        if (contacts.isEmpty()) {
            LOGGER.info("No contacts found.");
            return new ArrayList<>();
        }
        LOGGER.info("Fetched {} contacts.", contacts.size());
        return contacts;
    }

    @Override
    public Contact readByContact(String contact) {
        LOGGER.info("Fetching user by contact: {}", contact);
        return userRepository.findUserByContact(contact).orElseThrow(
                () -> {
                    LOGGER.error("Contact with contact {} not found", contact);
                    return new EntityNotFoundException("Contact with contact " + contact + " not found");
                }
        );
    }
}
