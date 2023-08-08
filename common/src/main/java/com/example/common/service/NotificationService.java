package com.example.common.service;


import com.example.common.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification create(Notification user);
    Notification readById(long id);
    Notification update(Notification user);
    void delete(Notification user);
    List<Notification> getAll();
    Notification readByName(String name);
}
