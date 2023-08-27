package org.example.controller;

import com.example.common.request.DataRequest;
import com.example.common.request.SendRequest;
import com.example.common.service.ContactService;
import com.example.common.service.NotificationService;
import org.example.kafka.SenderProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/ENS-Ukraine")
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private SenderProducer senderProducer;

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

        List<SendRequest> sendRequestList = SendRequest.transformToSendRequest(dataRequest);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (SendRequest sendRequest : sendRequestList) {
            executorService.submit(() -> {
                long threadId = Thread.currentThread().getId();
                LOGGER.info("Thread {} is sending a message: {}", threadId, sendRequest);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LOGGER.error("Thread {} was interrupted.", threadId);
                    Thread.currentThread().interrupt();
                }

                senderProducer.sendMessage(sendRequest);

                LOGGER.info("Thread {} sent the message successfully.", threadId);
            });
        }

        executorService.shutdown();
    }
}