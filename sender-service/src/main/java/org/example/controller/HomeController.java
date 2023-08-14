package org.example.controller;

import com.example.common.repository.DataRequest;
import com.example.common.service.ContactService;
import com.example.common.service.NotificationService;
import org.example.service.DataRequestService;
import org.example.service.email.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ENS-Ukraine")
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private DataRequestService service;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping
    public ResponseEntity<?> home() {
        LOGGER.info("Received request to get home data.");
        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notificationService.getAll());
        response.put("users", contactService.getAll());
        LOGGER.debug("Sending response with home data: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public void sendMessage(@Valid @RequestBody DataRequest dataRequest) {
        LOGGER.info("Received a data request message: {}", dataRequest);
        service.sendDataRequest(dataRequest);
        LOGGER.info("Data request message sent successfully.");
    }
}