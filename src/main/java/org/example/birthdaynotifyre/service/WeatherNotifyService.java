package org.example.birthdaynotifyre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherNotifyService {

    private final NotificationSender notificationSender;
    private final SubscriptionService subscriptionService;

    /**
     * Автоматическая отправка погоды каждый день в 7:00 по Москве
     */
    @Scheduled(cron = "0 0 7 * * ?", zone = "Europe/Moscow")
    public void sendMorningWeather() {
        log.info("Запуск автоматической отправки погоды...");

        Set<String> subscribedChats = subscriptionService.getAllWeatherSubscriptions();

        if (subscribedChats.isEmpty()) {
            log.warn("Нет подписанных чатов для отправки погоды");
            return;
        }

        log.info("Найдено {} подписчиков для отправки погоды", subscribedChats.size());

        for (String chatId : subscribedChats) {
            try {
                notificationSender.sendWeatherToChat(chatId);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Поток был прерван во время отправки погоды");
                break;
            } catch (Exception e) {
                log.error("Ошибка при отправке погоды в чат {}: {}", chatId, e.getMessage());
            }
        }

        log.info("Автоматическая отправка погоды завершена");
    }

    /**
     * Отправить тестовое уведомление о погоде
     */
    public void sendTestWeatherNotification(String chatId) {
        try {
            notificationSender.sendWeatherToChat(chatId);
            log.info("Тестовое уведомление о погоде отправлено в чат: {}", chatId);
        } catch (Exception e) {
            log.error("Ошибка при отправке тестового уведомления в чат {}: {}", chatId, e.getMessage());
        }
    }
}