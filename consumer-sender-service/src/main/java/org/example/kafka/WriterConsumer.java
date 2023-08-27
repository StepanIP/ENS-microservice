package org.example.kafka;

import com.example.common.model.Contact;
import com.example.common.request.WriterRequest;
import com.example.common.service.ContactService;
import com.example.common.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WriterConsumer {

    final ContactService contactService;

    final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderConsumer.class);

    public WriterConsumer(ContactService contactService, UserService userService) {
        this.contactService = contactService;
        this.userService = userService;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.name2}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "${spring.kafka.listener.concurrency}"
    )
    public void consume(WriterRequest writerRequest){
        for(String contact : writerRequest.getContacts()){
            Contact contact1 = new Contact(contact, userService.readByEmail(writerRequest.getOwnerName()));
            contactService.create(contact1);
            LOGGER.info(String.format("Writer successfully wrote contact => %s from user => %s", contact, writerRequest.getOwnerName()));
        }
    }

}
