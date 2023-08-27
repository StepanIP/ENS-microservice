package org.example.kafka;

import com.example.common.request.WriterRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class WriterProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderProducer.class);
    private final NewTopic topic2;
    private final KafkaTemplate<String, WriterRequest> kafkaTemplate;

    public WriterProducer(@Qualifier("topic2") NewTopic topic2, KafkaTemplate<String, WriterRequest> kafkaTemplate) {
        this.topic2 = topic2;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(WriterRequest writerRequest){
        LOGGER.info(String.format("Send event => %s", writerRequest.toString()));

        Message<WriterRequest> message = MessageBuilder
                .withPayload(writerRequest)
                .setHeader(KafkaHeaders.TOPIC, topic2.name())
                .build();

        kafkaTemplate.send(message);
    }
}
