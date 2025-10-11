package org.example.birthdaynotifyre.service;

import lombok.extern.slf4j.Slf4j;
import org.example.birthdaynotifyre.dto.FriendDto;
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
public class TelegramBotService extends TelegramLongPollingBot {

    private enum DialogState {
        WAITING_FOR_FULLNAME,
        WAITING_FOR_BIRTHDATE,
        NONE
    }

    private final Map<String, DialogState> userStates = new HashMap<>();
    private final Map<String, String> tempUserData = new HashMap<>();

    private final FriendService friendService;

    @Autowired
    public TelegramBotService(@Value("${telegram.token}") String botToken,
                              FriendService friendService) {
        super(botToken);
        this.friendService = friendService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            String userName = update.getMessage().getFrom().getFirstName();

            log.info("Получено сообщение от {}: {}", userName, messageText);

            // Проверяем состояние диалога
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
                    sendMessage("Доступные команды:\n" +
                            "/start - начать работу\n" +
                            "/help - помощь\n" +
                            "/add - добавить нового друга\n" +
                            "/today - посмотреть у кого сегодня день рождения\n" +
                            "/cancel - отменить текущую операцию", chatId);
                    break;
                case "/add":
                    startAddDialog(chatId);
                    break;
                case "/today":
                    showBirthdaysToday(chatId);
                    break;
                case "/cancel":
                    sendMessage("Нет активных операций для отмены.", chatId);
                    break;
                default:
                    sendMessage("Неизвестная команда. Используйте /help для списка команд.", chatId);
            }
        }
    }

    private void startAddDialog(String chatId) {
        userStates.put(chatId, DialogState.WAITING_FOR_FULLNAME);
        sendMessage("Введите ФИО нового друга (в формате: Фамилия Имя Отчество):\n" +
                "Для отмены введите /cancel", chatId);
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
        // Проверяем команду отмены
        if ("/cancel".equalsIgnoreCase(messageText)) {
            cancelDialog(chatId, currentState);
            return;
        }

        switch (currentState) {
            case WAITING_FOR_FULLNAME:
                if (isValidFullName(messageText)) {
                    // Сохраняем ФИО во временные данные
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
                    // Получаем ФИО из временных данных
                    String fullName = tempUserData.get(chatId + "_fullName");
                    if (fullName != null) {
                        saveFriend(chatId, fullName, messageText);
                    } else {
                        sendMessage("Ошибка: данные ФИО не найдены. Начните добавление заново с команды /add", chatId);
                        log.error("Данные ФИО не найдены для chatId: {}", chatId);
                    }
                    // Очищаем временные данные в любом случае
                    userStates.remove(chatId);
                    tempUserData.remove(chatId + "_fullName");
                } else {
                    sendMessage("Неверный формат даты. Пожалуйста, введите дату в формате ГГГГ-ММ-ДД (например: 1990-05-15):\n" +
                            "Для отмены введите /cancel", chatId);
                }
                break;
        }
    }

    private void cancelDialog(String chatId, DialogState currentState) {
        String message;
        String fullName = null;

        switch (currentState) {
            case WAITING_FOR_FULLNAME:
                message = "Добавление нового друга отменено. ФИО не было введено.";
                break;
            case WAITING_FOR_BIRTHDATE:
                // Получаем ФИО из временных данных
                fullName = tempUserData.get(chatId + "_fullName");
                if (fullName != null) {
                    message = "Добавление друга '" + fullName + "' отменено. Данные не сохранены.";
                } else {
                    message = "Добавление друга отменено. Данные не сохранены.";
                    log.warn("ФИО не найдено в tempUserData для chatId: {}", chatId);
                }
                break;
            default:
                message = "Операция отменена.";
        }

        // Очищаем состояние и временные данные
        userStates.remove(chatId);
        tempUserData.remove(chatId + "_fullName");

        sendMessage(message, chatId);
        log.info("Пользователь {} отменил операцию на этапе: {}, ФИО: {}",
                chatId, currentState, fullName != null ? fullName : "не указано");
    }

    private int calculateAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        int age = today.getYear() - birthDate.getYear();

        // Если день рождения еще не наступил в этом году, вычитаем 1 год
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
            // Создаем DTO для нового друга
            FriendDto friendDto = FriendDto.builder()
                    .fullName(fullName)
                    .birthDate(java.time.LocalDate.parse(birthDateStr))
                    .build();

            // Сохраняем в базу данных
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

    public void sendMessage(String message, String chatID){
        SendMessage sendMessage = new SendMessage();
        if (chatID != null) {
            sendMessage.setChatId(chatID);
            sendMessage.setText(message);
            try {
                execute(sendMessage);
//                log.atLevel(Level.INFO).log("Сообщение отправлено в телеграм");
            } catch (TelegramApiException e) {
                log.atLevel(Level.WARN).log("Ошибка при отправке сообщения в телеграм: " + e.getMessage());
            }
        }
    }
}