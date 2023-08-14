package com.example.common.transformer;

import com.example.common.model.Notification;
import com.example.common.request.NotificationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NotificationTransformerTest {

    @Test
    public void testTransformToEntity_ValidNotificationRequest_Success() {
        NotificationRequest notificationRequest = new NotificationRequest("Test", "This is a test notification");

        Notification notification = NotificationTransformer.transformToEntity(notificationRequest);

        assertNotNull(notification);
        assertEquals(notificationRequest.getName(), notification.getName());
        assertEquals(notificationRequest.getMessage(), notification.getMessage());
    }

    @Test
    public void testTransformToDto_ValidNotification_Success() {
        Notification notification = new Notification();
        notification.setName("Test");
        notification.setMessage("This is a test notification");

        NotificationRequest notificationRequest = NotificationTransformer.transformToDto(notification);

        assertNotNull(notificationRequest);
        assertEquals(notification.getName(), notificationRequest.getName());
        assertEquals(notification.getMessage(), notificationRequest.getMessage());
    }
}
