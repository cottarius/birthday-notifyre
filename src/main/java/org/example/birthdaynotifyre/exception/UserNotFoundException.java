package org.example.birthdaynotifyre.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message, String telegramId) {
        super(message);
    }
}
