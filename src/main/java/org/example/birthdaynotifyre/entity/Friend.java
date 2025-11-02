package org.example.birthdaynotifyre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Friend extends BaseEntity {

    /**
     * Полное имя.
     */
    private String fullName;

    /**
     * Дата рождения.
     */
    private LocalDate birthDate;

    /**
     * Пользователь, которому принадлежит друг.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
