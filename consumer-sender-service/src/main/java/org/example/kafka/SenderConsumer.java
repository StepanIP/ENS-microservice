package org.example.kafka;

import com.example.common.request.SendRequest;
import org.example.service.DataRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SenderConsumer {

    final DataRequestService dataRequestService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderConsumer.class);

    public SenderConsumer(DataRequestService dataRequestService) {
        this.dataRequestService = dataRequestService;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "${spring.kafka.listener.concurrency}"
    )
    public void consume(SendRequest sendRequest){
        dataRequestService.sendDataRequest(sendRequest);
        LOGGER.info(String.format("Sender event sent in service=> %s", sendRequest));
    }

}
