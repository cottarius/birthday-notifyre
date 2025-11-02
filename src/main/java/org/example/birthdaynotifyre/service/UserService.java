package org.example.birthdaynotifyre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.birthdaynotifyre.entity.User;
import org.example.birthdaynotifyre.exception.UserNotFoundException;
import org.example.birthdaynotifyre.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseGet(() -> {
                    log.warn("Пользователь с telegramId: {} не найден", telegramId);
                    return null;
                });
    }

    public User createUser(String telegramId,  String fullUserName) {
        User user = new User();
        user.setTelegramId(telegramId);
        user.setFullName(fullUserName);
        userRepository.save(user);
        log.info("Сохранен новый пользователь: {}", user);
        return user;
    }
}
