package org.example.birthdaynotifyre.service;

import lombok.extern.slf4j.Slf4j;
import org.example.birthdaynotifyre.dto.friend.FriendDto;
import org.example.birthdaynotifyre.entity.Friend;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot implements NotificationSender {

    private enum DialogState {
        WAITING_FOR_FULLNAME,
        WAITING_FOR_BIRTHDATE,
        WAITING_FOR_CITY,
        NONE
    }

    private final Map<String, DialogState> userStates = new HashMap<>();
    private final Map<String, String> tempUserData = new HashMap<>();

    private final FriendService friendService;
    private final WeatherService weatherService;
    private final SubscriptionService subscriptionService;

    private static final String CITY_TAGANROG = "Taganrog";

    @Autowired
    public TelegramBotService(@Value("${telegram.token}") String botToken,
                              FriendService friendService,
                              WeatherService weatherService,
                              SubscriptionService subscriptionService) {

        super(botToken);
        this.friendService = friendService;
        this.weatherService = weatherService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            String userName = update.getMessage().getFrom().getFirstName();

            log.info("Получено сообщение от {}: {}", userName, messageText);

            DialogState currentState = userStates.getOrDefault(chatId, DialogState.NONE);

            if (currentState != DialogState.NONE) {
                handleDialog(chatId, messageText, currentState);
                return;
            }

            switch (messageText) {
                case "/start":
                    sendMessage("Привет, " + userName + "! Я бот для уведомлений о днях рождения.", chatId);
                    break;
                case "/help":
                    sendMessage(showHelp(), chatId);
                    break;
                case "/add":
                    startAddDialog(chatId);
                    break;
                case "/today":
                    showBirthdaysToday(chatId);
                    break;
                case "/weather_taganrog":
                    getWeatherInTaganrog(chatId);
                    break;
                case "/weather":
                    startWeatherDialog(chatId);
                    break;
                case "/subscribe":
                    subscribeToNotifications(chatId);
                    break;
                case "/unsubscribe":
                    unsubscribeFromNotifications(chatId);
                    break;
                case "/subscriptions":
                    showSubscriptionStatus(chatId);
                    break;
                case "/cancel":
                    sendMessage("Нет активных операций для отмены.", chatId);
                    break;
                default:
                    sendMessage("Неизвестная команда. Используйте /help для списка команд.", chatId);
            }
        }
    }

    private void subscribeToNotifications(String chatId) {
        boolean subscribed = subscriptionService.subscribeToWeather(chatId);
        if (subscribed) {
            sendMessage("""
                    ✅ Вы подписались на утренние уведомления о погоде!
                    Каждый день в 7:00 по Москве вы будете получать погоду в Таганроге.
                    Для отписки используйте /unsubscribe""", chatId);
        } else {
            sendMessage("ℹ️ Вы уже подписаны на утренние уведомления о погоде!", chatId);
        }
    }

    private void unsubscribeFromNotifications(String chatId) {
        boolean unsubscribed = subscriptionService.unsubscribeFromWeather(chatId);
        if (unsubscribed) {
            sendMessage("❌ Вы отписались от утренних уведомлений о погоде.\n" +
                    "Для повторной подписки используйте /subscribe", chatId);
        } else {
            sendMessage("ℹ️ Вы не были подписаны на утренние уведомления о погоде.", chatId);
        }
    }

    private void startWeatherDialog(String chatId) {
        userStates.put(chatId, DialogState.WAITING_FOR_CITY);
        sendMessage("Введите название города на английском языке:\n" +
                "Для отмены введите /cancel", chatId);
    }

    private static String showHelp() {
        return """
            Доступные команды:
            /start - начать работу
            /help - помощь
            /add - добавить нового друга
            /today - посмотреть у кого сегодня день рождения
            /cancel - отменить текущую операцию
            /weather_taganrog - показывает погоду в Таганроге
            /weather - показывает погоду в заданном вами городе
            /subscribe - подписаться на утреннюю рассылку погоды в Таганроге (7:00)
            /unsubscribe - отписаться от утренней рассылки погоды
            /subscriptions - показать статус подписки
            """;
    }

    private void getWeatherInTaganrog(String chatId) {
        String weatherInfo = weatherService.getWeatherForCity(CITY_TAGANROG);
        sendMessage(weatherInfo, chatId);
    }

    private void getWeatherInCurrentCity(String chatId, String city) {
        String weatherInfo = weatherService.getWeatherForCity(city);
        sendMessage(weatherInfo, chatId);
    }

    private void startAddDialog(String chatId) {
        userStates.put(chatId, DialogState.WAITING_FOR_FULLNAME);
        sendMessage("Введите ФИО нового друга (в формате: Фамилия Имя Отчество):\n" +
                "Для отмены введите /cancel", chatId);
    }

    private void showSubscriptionStatus(String chatId) {
        boolean isSubscribed = subscriptionService.isSubscribedToWeather(chatId);
        if (isSubscribed) {
            sendMessage("✅ Вы подписаны на утренние уведомления о погоде!\n" +
                    "Каждый день в 7:00 по Москве вы получаете погоду в Таганроге.", chatId);
        } else {
            sendMessage("❌ Вы не подписаны на утренние уведомления о погоде.\n" +
                    "Используйте /subscribe чтобы подписаться.", chatId);
        }
    }

    private void showBirthdaysToday(String chatId) {
        try {
            List<Friend> friendsWithBirthdayToday = friendService.findFriendsWithBirthdayToday();

            if (friendsWithBirthdayToday.isEmpty()) {
                sendMessage("Сегодня никто не празднует день рождения! 🎉", chatId);
            } else {
                StringBuilder message = new StringBuilder();
                message.append("🎂 Сегодня день рождения у:\n\n");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                for (Friend friend : friendsWithBirthdayToday) {
                    String birthDateFormatted = friend.getBirthDate().format(formatter);
                    int age = calculateAge(friend.getBirthDate());

                    message.append("👤 ").append(friend.getFullName()).append("\n");
                    message.append("📅 Дата рождения: ").append(birthDateFormatted).append("\n");
                    message.append("🎊 Исполняется: ").append(age).append(" лет\n");
                    message.append("---\n");
                }

                sendMessage(message.toString(), chatId);
            }

        } catch (Exception e) {
            log.error("Ошибка при получении списка дней рождений: {}", e.getMessage());
            sendMessage("Произошла ошибка при получении списка дней рождений. Попробуйте позже.", chatId);
        }
    }

    private void handleDialog(String chatId, String messageText, DialogState currentState) {
        if ("/cancel".equalsIgnoreCase(messageText)) {
            cancelDialog(chatId, currentState);
            return;
        }

        switch (currentState) {
            case WAITING_FOR_FULLNAME:
                if (isValidFullName(messageText)) {
                    tempUserData.put(chatId + "_fullName", messageText.trim());
                    userStates.put(chatId, DialogState.WAITING_FOR_BIRTHDATE);
                    sendMessage("Теперь введите дату рождения в формате ГГГГ-ММ-ДД (например: 1990-05-15):\n" +
                            "Для отмены введите /cancel", chatId);
                } else {
                    sendMessage("Пожалуйста, введите ФИО в правильном формате (минимум Фамилия и Имя):\n" +
                            "Для отмены введите /cancel", chatId);
                }
                break;

            case WAITING_FOR_BIRTHDATE:
                if (isValidDate(messageText)) {
                    String fullName = tempUserData.get(chatId + "_fullName");

                    if (fullName != null) {
                        saveFriend(chatId, fullName, messageText);
                    } else {
                        sendMessage("Ошибка: данные ФИО не найдены. Начните добавление заново с команды /add", chatId);
                        log.error("Данные ФИО не найдены для chatId: {}", chatId);
                    }

                    userStates.remove(chatId);
                    tempUserData.remove(chatId + "_fullName");
                } else {
                    sendMessage("Неверный формат даты. Пожалуйста, введите дату в формате ГГГГ-ММ-ДД (например: 1990-05-15):\n" +
                            "Для отмены введите /cancel", chatId);
                }
                break;

            case WAITING_FOR_CITY:
                if (isValidCity(messageText)) {
                    getWeatherInCurrentCity(chatId, messageText.trim());
                    userStates.remove(chatId);
                } else {
                    sendMessage("Пожалуйста, введите корректное название города на английском языке:\n" +
                            "Для отмены введите /cancel", chatId);
                }
                break;
        }
    }

    private boolean isValidCity(String city) {
        return city != null && city.trim().length() >= 2;
    }

    private void cancelDialog(String chatId, DialogState currentState) {
        String message;
        String fullName = null;

        switch (currentState) {
            case WAITING_FOR_FULLNAME:
                message = "Добавление нового друга отменено. ФИО не было введено.";
                break;
            case WAITING_FOR_BIRTHDATE:
                fullName = tempUserData.get(chatId + "_fullName");
                if (fullName != null) {
                    message = "Добавление друга '" + fullName + "' отменено. Данные не сохранены.";
                } else {
                    message = "Добавление друга отменено. Данные не сохранены.";
                    log.warn("ФИО не найдено в tempUserData для chatId: {}", chatId);
                }
                break;
            case WAITING_FOR_CITY:
                message = "Запрос погоды отменен. Город не был введен.";
                break;
            default:
                message = "Операция отменена.";
        }

        userStates.remove(chatId);
        tempUserData.remove(chatId + "_fullName");

        sendMessage(message, chatId);
        log.info("Пользователь {} отменил операцию на этапе: {}, ФИО: {}",
                chatId, currentState, fullName != null ? fullName : "не указано");
    }

    private int calculateAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        int age = today.getYear() - birthDate.getYear();

        if (today.getMonthValue() < birthDate.getMonthValue() ||
                (today.getMonthValue() == birthDate.getMonthValue() && today.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }

        return age;
    }

    private boolean isValidFullName(String fullName) {
        return fullName != null && fullName.trim().split("\\s+").length >= 2;
    }

    private boolean isValidDate(String dateStr) {
        try {
            java.time.LocalDate.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void saveFriend(String chatId, String fullName, String birthDateStr) {
        try {
            FriendDto friendDto = FriendDto.builder()
                    .fullName(fullName)
                    .birthDate(java.time.LocalDate.parse(birthDateStr))
                    .build();

            friendService.create(friendDto);

            sendMessage("Друг успешно добавлен! ✅\n" +
                    "ФИО: " + fullName + "\n" +
                    "Дата рождения: " + birthDateStr, chatId);

            log.info("Добавлен новый друг: {}, дата рождения: {}", fullName, birthDateStr);

        } catch (Exception e) {
            log.error("Ошибка при добавлении друга: {}", e.getMessage());
            sendMessage("Произошла ошибка при добавлении друга. Попробуйте еще раз.", chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return "Cotarius_bot";
    }

    @Override
    public void sendMessage(String message, String chatID) {
        SendMessage sendMessage = new SendMessage();
        if (chatID != null) {
            sendMessage.setChatId(chatID);
            sendMessage.setText(message);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.atLevel(Level.WARN).log("Ошибка при отправке сообщения в телеграм: " + e.getMessage());
            }
        }
    }

    @Override
    public void sendWeatherToChat(String chatId) {
        try {
            String weatherInfo = weatherService.getWeatherForCity("Taganrog");
            String message = "🌅 Доброе утро! Вот погода в Таганроге на сегодня:\n\n" + weatherInfo;
            sendMessage(message, chatId);
            log.info("Погода отправлена в чат: {}", chatId);
        } catch (Exception e) {
            log.error("Ошибка при отправке погоды в чат {}: {}", chatId, e.getMessage());
        }
    }
}