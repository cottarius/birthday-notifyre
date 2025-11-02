package org.example.birthdaynotifyre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscriptions")
@EqualsAndHashCode(callSuper = true)
public class Subscription extends BaseEntity {

    private String chatId;

    private boolean subscribedToWeather;
}
