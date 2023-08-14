package com.example.common.service;

import com.example.common.exception.NullEntityReferenceException;
import com.example.common.model.Notification;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    public void createNotificationTest(){
        Notification notification = new Notification();
        notification.setName("Test");
        notification.setMessage("Test");
        List<Notification> before = notificationService.getAll();

        notificationService.create(notification);

        assertEquals(before.size()+1, notificationService.getAll().size());
        assertEquals(notification, notificationService.readByName(notification.getName()));
    }

    @Test
    public void createNotificationExceptionTest(){
        assertThrows(NullEntityReferenceException.class, () -> notificationService.create(null));
    }

    @Test
    public void readByIdNotificationTest(){
        Notification notification = new Notification();
        notification.setName("Test");
        notification.setMessage("Test");

        notificationService.create(notification);

        assertEquals(notification, notificationService.readById(notification.getId()));
    }

    @Test
    public void readByIdNotificationExceptionTest(){
        Notification notification = new Notification();
        notification.setName("Test");
        notification.setMessage("Test");

        notificationService.create(notification);

        assertThrows(EntityNotFoundException.class, () -> notificationService.readById(notification.getId() + 1));
    }

    @Test
    public void updateNotificationTest(){
        Notification notification = new Notification();
        notification.setName("Test");
        notification.setMessage("Test");

        notificationService.create(notification);

        Notification updateNotification = notificationService.readById(notification.getId());
        updateNotification.setName("New");
        updateNotification.setMessage("New");

        notificationService.update(updateNotification);

        assertEquals(updateNotification, notificationService.readById(updateNotification.getId()));
    }

    @Test
    public void updateNotificationExceptionTest(){
        assertThrows(NullEntityReferenceException.class, () -> notificationService.update(null));
    }

    @Test
    public void deleteNotificationTest(){
        Notification notification = new Notification();
        notification.setName("Test");
        notification.setMessage("Test");

        notificationService.create(notification);
        List<Notification> before = notificationService.getAll();

        notificationService.delete(notification);

        assertEquals(before.size()-1, notificationService.getAll().size());
        assertThrows(EntityNotFoundException.class, () -> notificationService.readByName(notification.getName()));
    }

    @Test
    public void getAllNotificationsTest(){
        List<Notification> expected = new ArrayList<>();
        Notification notification = new Notification();
        notification.setName("Test");
        notification.setMessage("Test");
        expected.add(notification);

        Notification notification1 = new Notification();
        notification1.setName("Testtwo");
        notification1.setMessage("Testtwo");
        expected.add(notification1);

        notificationService.create(notification);
        notificationService.create(notification1);

        assertEquals(expected.size(), notificationService.getAll().size());
        assertEquals(expected, notificationService.getAll());
    }

    @Test
    public void getAllEmptyNotificationsTest(){
        assertEquals(new ArrayList<>(), notificationService.getAll());
    }

    @Test
    public void readByNameNotificationTest(){
        Notification notification = new Notification();
        notification.setName("Test");
        notification.setMessage("Test");

        notificationService.create(notification);

        assertEquals(notification, notificationService.readByName(notification.getName()));
    }

    @Test
    public void readByNameNotificationExceptionTest(){
        Notification notification = new Notification();
        notification.setName("Test");
        notification.setMessage("Test");

        notificationService.create(notification);

        assertThrows(EntityNotFoundException.class, () -> notificationService.readByName(notification.getName()+1));
    }

}
