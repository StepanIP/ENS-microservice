package com.example.common.request;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SendRequestTests {

    @Test
    public void testTransformToSendRequest() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setContacts(List.of("contact1", "contact2"));
        dataRequest.setMessage("test message");

        List<SendRequest> sendRequests = SendRequest.transformToSendRequest(dataRequest);

        assertEquals(2, sendRequests.size());
        assertEquals("contact1", sendRequests.get(0).getContact());
        assertEquals("test message", sendRequests.get(0).getMessage());
        assertEquals("contact2", sendRequests.get(1).getContact());
        assertEquals("test message", sendRequests.get(1).getMessage());
    }
}
