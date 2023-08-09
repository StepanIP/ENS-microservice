package com.example.common.request;

public class NotificationRequest {
    String name;
    String message;

    public NotificationRequest() {
    }

    public NotificationRequest(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
