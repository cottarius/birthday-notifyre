package org.example.birthdaynotifyre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.birthdaynotifyre.entity.Friend;
import org.example.birthdaynotifyre.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для нотификации о днях рождений знакомых.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BirthdayNotifyService {

    private final FriendRepository friendRepository;
    private final TelegramBotService telegramBotService;
    private static final String NO_BIRTHDAY_FOUND = "Сегодня нет дней рождения";
    
    @Value("${telegram.chat_id}")
    private String chatId;

    /**
     * Проверяет есть сегодня день рождения у знакомых.
     */
    @Scheduled(cron = "0 0 9 * * ?") // Каждый день в 9:00
    public void checkBirthdaysAndNotify() {
        log.info("Начинаю проверку дней рождения...");
        
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        List<Friend> friendsWithBirthday = friendRepository.findByBirthDateMonthAndDay(month, day);

        String message;
        
        if (friendsWithBirthday.isEmpty()) {
            log.info(NO_BIRTHDAY_FOUND);
            return;
        }
        
        log.info("Найдено {} друзей с днем рождения сегодня", friendsWithBirthday.size());
        
        for (Friend friend : friendsWithBirthday) {
            message = formatBirthdayMessage(friend);
            telegramBotService.sendMessage(message, chatId);
            log.info("Отправлено уведомление о дне рождения для: {}", getFullName(friend));
        }
    }

    /**
     * Формирует сообщение для отправки уведомления о знакомых с Днем Рождения в телеграм.
     *
     * @param friend сущность знакомого
     * @return сообщение для отправки уведомления о знакомых с Днем Рождения в телеграм
     */
    private String formatBirthdayMessage(Friend friend) {
        String fullName = getFullName(friend);
        return String.format("🎉 Сегодня день рождения у %s! 🎂\nПоздравьте с праздником!", fullName);
    }

    /**
     * Получает полное имя знакомого в строковом представлении.
     *
     * @param friend сущность знакомого
     * @return полное имя знакомого в строковом представлении
     */
    private String getFullName(Friend friend) {
        return Optional.ofNullable(friend)
                .map(Friend::getFullName)
                .orElseThrow(() -> new RuntimeException("Что-то пошло не так..."));
    }
}
