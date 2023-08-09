package org.example.controller;

import com.example.common.request.ContactRequest;
import com.example.common.model.Contact;
import com.example.common.service.ContactService;
import com.example.common.service.UserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/ENS-Ukraine/contact")
public class ContactController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    ContactService contactService;

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public void addContacts(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        LOGGER.info("Received request to add contacts from an Excel file.");

        if (!file.isEmpty() && (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx"))) {
            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    String contact = row.getCell(0).getStringCellValue();

                    Contact contact1 = new Contact();
                    contact1.setContact(contact);
                    contact1.setOwner(userService.readByEmail(principal.getName()));
                    contactService.create(contact1);
                }
            }
            LOGGER.info("Contacts added successfully.");
        } else {
            LOGGER.error("Invalid file or format.");
            throw new IllegalArgumentException("Invalid file or format.");
        }
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

