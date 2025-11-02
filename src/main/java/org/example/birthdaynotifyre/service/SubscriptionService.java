package org.example.birthdaynotifyre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.birthdaynotifyre.entity.Subscription;
import org.example.birthdaynotifyre.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    /**
     * Подписать чат на утренние уведомления о погоде
     */
    @Transactional
    public boolean subscribeToWeather(String chatId) {
        try {
            Optional<Subscription> existingSubscription = subscriptionRepository.findByChatId(chatId);

            if (existingSubscription.isPresent()) {
                Subscription subscription = existingSubscription.get();
                if (!subscription.isSubscribedToWeather()) {
                    subscription.setSubscribedToWeather(true);
                    subscriptionRepository.save(subscription);
                    log.info("Чат {} возобновил подписку на утренние уведомления о погоде", chatId);
                    return true;
                }
                log.info("Чат {} уже был подписан на утренние уведомления о погоде", chatId);
                return false;
            } else {
                // Новая подписка
                Subscription newSubscription = new Subscription();
                newSubscription.setChatId(chatId);
                newSubscription.setSubscribedToWeather(true);
                subscriptionRepository.save(newSubscription);
                log.info("Чат {} подписан на утренние уведомления о погоде", chatId);
                return true;
            }
        } catch (Exception e) {
            log.error("Ошибка при подписке чата {}: {}", chatId, e.getMessage());
            return false;
        }
    }

    /**
     * Отписать чат от утренних уведомлений о погоде
     */
    @Transactional
    public boolean unsubscribeFromWeather(String chatId) {
        try {
            Optional<Subscription> subscriptionOpt = subscriptionRepository.findByChatId(chatId);
            if (subscriptionOpt.isPresent() && subscriptionOpt.get().isSubscribedToWeather()) {
                Subscription subscription = subscriptionOpt.get();
                subscription.setSubscribedToWeather(false);
                subscriptionRepository.save(subscription);
                log.info("Чат {} отписан от утренних уведомлений о погоде", chatId);
                return true;
            }
            log.info("Чат {} не был подписан на утренние уведомления о погоде", chatId);
            return false;
        } catch (Exception e) {
            log.error("Ошибка при отписке чата {}: {}", chatId, e.getMessage());
            return false;
        }
    }

    /**
     * Проверить, подписан ли чат на уведомления о погоде
     */
    public boolean isSubscribedToWeather(String chatId) {
        return subscriptionRepository.existsByChatIdAndSubscribedToWeatherTrue(chatId);
    }

    /**
     * Получить все подписки на погоду
     */
    public Set<String> getAllWeatherSubscriptions() {
        return subscriptionRepository.findBySubscribedToWeatherTrue()
                .stream()
                .map(Subscription::getChatId)
                .collect(Collectors.toSet());
    }

    /**
     * Получить количество подписчиков на погоду
     */
    public int getWeatherSubscriptionsCount() {
        return subscriptionRepository.findBySubscribedToWeatherTrue().size();
    }

    /**
     * Очистить все подписки (для тестирования)
     */
    @Transactional
    public void clearAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        for (Subscription subscription : subscriptions) {
            subscription.setSubscribedToWeather(false);
        }
        subscriptionRepository.saveAll(subscriptions);
        log.info("Все подписки очищены");
    }

    /**
     * Полная очистка записей из БД (осторожно!)
     */
    @Transactional
    public void deleteAllSubscriptions() {
        subscriptionRepository.deleteAll();
        log.info("Все записи подписок удалены из БД");
    }
}