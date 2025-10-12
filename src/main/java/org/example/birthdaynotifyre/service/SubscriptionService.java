package org.example.birthdaynotifyre.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SubscriptionService {

    private final Set<String> weatherSubscriptions = ConcurrentHashMap.newKeySet();

    /**
     * Подписать чат на утренние уведомления о погоде
     */
    public boolean subscribeToWeather(String chatId) {
        boolean added = weatherSubscriptions.add(chatId);
        if (added) {
            log.info("Чат {} подписан на утренние уведомления о погоде", chatId);
        } else {
            log.info("Чат {} уже был подписан на утренние уведомления о погоде", chatId);
        }
        return added;
    }

    /**
     * Отписать чат от утренних уведомлений о погоде
     */
    public boolean unsubscribeFromWeather(String chatId) {
        boolean removed = weatherSubscriptions.remove(chatId);
        if (removed) {
            log.info("Чат {} отписан от утренних уведомлений о погоде", chatId);
        } else {
            log.info("Чат {} не был подписан на утренние уведомления о погоде", chatId);
        }
        return removed;
    }

    /**
     * Проверить, подписан ли чат на уведомления о погоде
     */
    public boolean isSubscribedToWeather(String chatId) {
        return weatherSubscriptions.contains(chatId);
    }

    /**
     * Получить все подписки на погоду
     */
    public Set<String> getAllWeatherSubscriptions() {
        return new HashSet<>(weatherSubscriptions);
    }

    /**
     * Получить количество подписчиков на погоду
     */
    public int getWeatherSubscriptionsCount() {
        return weatherSubscriptions.size();
    }

    /**
     * Очистить все подписки (для тестирования)
     */
    public void clearAllSubscriptions() {
        weatherSubscriptions.clear();
        log.info("Все подписки очищены");
    }
}