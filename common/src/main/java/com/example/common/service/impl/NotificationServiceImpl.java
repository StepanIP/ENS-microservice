package com.example.common.service.impl;


import com.example.common.exception.NullEntityReferenceException;
import com.example.common.model.Notification;
import com.example.common.repository.NotificationRepository;
import com.example.common.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification create(Notification notification) {
        LOGGER.info("Creating notification: {}", notification);
        if (notification != null) {
            Notification savedNotification = notificationRepository.save(notification);
            LOGGER.info("Notification created successfully.");
            return savedNotification;
        }
        LOGGER.error("Notification cannot be 'null'");
        throw new NullEntityReferenceException("Notification cannot be 'null'");
    }

    @Override
    public Notification readById(long id) {
        LOGGER.info("Fetching notification by ID: {}", id);
        return notificationRepository.findById(id).orElseThrow(
                () -> {
                    LOGGER.error("Notification with ID {} not found", id);
                    return new EntityNotFoundException("Notification with id " + id + " not found");
                }
        );
    }

    @Override
    public Notification update(Notification notification) {
        LOGGER.info("Updating notification: {}", notification);
        if (notification != null) {
            readById(notification.getId());
            Notification updatedNotification = notificationRepository.save(notification);
            LOGGER.info("Notification updated successfully.");
            return updatedNotification;
        }
        LOGGER.error("Notification cannot be 'null'");
        throw new NullEntityReferenceException("Notification cannot be 'null'");
    }

    @Override
    public void delete(Notification notification) {
        LOGGER.info("Deleting notification: {}", notification);
        notificationRepository.delete(notification);
        LOGGER.info("Notification deleted successfully.");
    }

    @Override
    public List<Notification> getAll() {
        LOGGER.info("Fetching all notifications.");
        List<Notification> notifications = notificationRepository.findAll();
        if (notifications.isEmpty()) {
            LOGGER.info("No notifications found.");
            return new ArrayList<>();
        }
        LOGGER.info("Fetched {} notifications.", notifications.size());
        return notifications;
    }

    @Override
    public Notification readByName(String name) {
        LOGGER.info("Fetching notification by name: {}", name);
        return notificationRepository.findUserByName(name).orElseThrow(
                () -> {
                    LOGGER.error("Notification with name {} not found", name);
                    return new EntityNotFoundException("Notification with name " + name + " not found");
                }
        );
    }
}
