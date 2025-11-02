package org.example.birthdaynotifyre.repository;

import org.example.birthdaynotifyre.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(String telegramId);
}
