package com.example.common.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class SendRequest {
    String contact;
    String message;

    public static List<SendRequest> transformToSendRequest(DataRequest dataRequest){
        List<SendRequest> sendRequests = new ArrayList<>();
        for(String contact :  dataRequest.getContacts()){
            sendRequests.add(new SendRequest(contact, dataRequest.getMessage()));
        }
        return sendRequests;
    }
}
