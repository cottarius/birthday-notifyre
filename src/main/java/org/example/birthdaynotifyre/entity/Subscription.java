package org.example.birthdaynotifyre.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatId;

    private boolean subscribedToWeather;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
