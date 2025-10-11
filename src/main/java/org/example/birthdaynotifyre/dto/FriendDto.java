package org.example.birthdaynotifyre.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO сущности Friend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO с данными друга")
public class FriendDto {

    /**
     * Полное имя.
     */
    @Schema(description = "Полное имя", example = "Иванов Иван Иванович")
    private String fullName;

    /**
     * Дата рождения.
     */
    @Schema(description = "Дата рождения", example = "1990-05-15")
    private LocalDate birthDate;
}
