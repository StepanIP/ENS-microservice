package com.example.common.transformer;


import com.example.common.request.NotificationRequest;
import com.example.common.model.Notification;

public class NotificationTransformer {

    public static Notification transformToEntity(NotificationRequest notificationRequest){
        Notification notification = new Notification();
        notification.setName(notificationRequest.getName());
        notification.setMessage(notificationRequest.getMessage());
        return notification;
    }

    public static NotificationRequest transformToDto(Notification notification){
        return new NotificationRequest(notification.getName(),notification.getMessage());
    }

}
