package org.example.kafka;

import com.example.common.request.SendRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class SenderProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SenderProducer.class);
    private final NewTopic topic;
    private final KafkaTemplate<String, SendRequest> kafkaTemplate;

    public SenderProducer(NewTopic topic, KafkaTemplate<String, SendRequest> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(SendRequest sendEvent){
        LOGGER.info(String.format("Send event => %s", sendEvent.toString()));

        Message<SendRequest> message = MessageBuilder
                .withPayload(sendEvent)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        kafkaTemplate.send(message);
    }
}
