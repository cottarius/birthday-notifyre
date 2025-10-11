package org.example.birthdaynotifyre.repository;

import org.example.birthdaynotifyre.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Репозиторий для работы с сущностью Friend.
 */
public interface FriendRepository extends JpaRepository<Friend, Long> {

    /**
     * Поиск человека по месяцу и дню из даты рождения.
     *
     * @param month месяц рождения
     * @param day день рождения
     * @return список знакомых, у которых сегодня день рождения
     */
    @Query("SELECT f FROM Friend f WHERE EXTRACT(MONTH FROM f.birthDate) = :month AND EXTRACT(DAY FROM f.birthDate) = :day")
    List<Friend> findByBirthDateMonthAndDay(@Param("month") int month, @Param("day") int day);
}
