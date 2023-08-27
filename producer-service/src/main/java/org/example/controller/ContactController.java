package org.example.controller;

import com.example.common.request.ContactRequest;
import com.example.common.request.WriterRequest;
import com.example.common.service.ContactService;
import com.example.common.service.UserService;
import org.example.kafka.WriterProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/ENS-Ukraine/contact")
public class ContactController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    ContactService contactService;

    @Autowired
    UserService userService;

    @Autowired
    private WriterProducer writerProducer;

    @PostMapping("/add")
    public void addContacts(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        LOGGER.info("Received request to add contacts from an Excel file.");
        WriterRequest writerRequest = new WriterRequest();
        writerRequest.getFromFile(file);
        writerRequest.setOwnerName(principal.getName());
        writerProducer.sendMessage(writerRequest);
        LOGGER.info("Sent message to kafka writer producer successfully");
    }

    @DeleteMapping("/delete")
    public void deleteContacts(@RequestBody ContactRequest user) {
        LOGGER.info("Received request to delete contact with contact: {}", user.getContact());
        contactService.delete(contactService.readByContact(user.getContact()));
        LOGGER.info("Contact deleted successfully.");
    }

    @PutMapping("/edit")
    public void editContacts(@RequestBody ContactRequest user) {
        LOGGER.info("Received request to edit contact with contact: {}", user.getContact());
        contactService.update(contactService.readByContact(user.getContact()));
        LOGGER.info("Contact edited successfully.");
    }
}

