package org.example.birthdaynotifyre.config;

import org.example.birthdaynotifyre.service.TelegramBotService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Конфигурационный класс для настройки и регистрации Telegram бота в приложении.
 *
 * @author olegprokopenko
 */
@Configuration
public class TelegramBotConfig {

    /**
     * Создает и регистрирует Telegram бота в API Telegram.
     *
     * @param telegramBotService сервис Telegram бота, который будет зарегистрирован.
     * @return экземпляр TelegramBotsApi с зарегистрированным ботом.
     * @throws TelegramApiException если произошла ошибка при регистрации бота.
     */
    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBotService telegramBotService) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(telegramBotService);
        return api;
    }
}
