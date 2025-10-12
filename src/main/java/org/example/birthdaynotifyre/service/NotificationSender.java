package org.example.birthdaynotifyre.service;

public interface NotificationSender {
    void sendMessage(String message, String chatId);
    void sendWeatherToChat(String chatId);
}