package org.example.birthdaynotifyre.repository;

import org.example.birthdaynotifyre.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByChatId(String chatId);
    boolean existsByChatIdAndSubscribedToWeatherTrue(String chatId);
    List<Subscription> findBySubscribedToWeatherTrue();
    void deleteByChatId(String chatId);
}
