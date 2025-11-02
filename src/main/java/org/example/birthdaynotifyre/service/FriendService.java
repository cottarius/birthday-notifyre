package org.example.birthdaynotifyre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.birthdaynotifyre.dto.friend.FriendDto;
import org.example.birthdaynotifyre.entity.Friend;
import org.example.birthdaynotifyre.mapper.FriendMapper;
import org.example.birthdaynotifyre.repository.FriendRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для работы с сущностью Friend.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;

    /**
     * Создание новой сущности Friend.
     *
     * @param friendDto DTO сущности Friend
     * @return новый экземпляр сущности Friend
     */
    public Friend create(FriendDto friendDto) {
        if (friendDto.getUserId() == null) {
            throw new IllegalArgumentException("userId не может быть null");
        }
        Friend friend = friendMapper.toEntity(friendDto);
        return friendRepository.save(friend);
    }

    /**
     * Получает список всех знакомых.
     *
     * @return список всех знакомых
     */
    public List<Friend> findAll() {
        return friendRepository.findAll();
    }

    /**
     * Получение списка знакомых, у которых сегодня День Рождения.
     *
     * @return список знакомых, у которых сегодня День Рождения.
     */
    public List<Friend> findFriendsWithBirthdayToday() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        return friendRepository.findByBirthDateMonthAndDay(month, day);
    }
}
