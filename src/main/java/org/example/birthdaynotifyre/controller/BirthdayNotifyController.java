package org.example.birthdaynotifyre.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.example.birthdaynotifyre.service.BirthdayNotifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с сервисом BirthdayNotifyService.
 */
@RestController
@RequestMapping("/api/birthdays")
@RequiredArgsConstructor
public class BirthdayNotifyController {

    private final BirthdayNotifyService birthdayNotifyService;

    @PostMapping("/check")
    @Operation(
            summary = "Проверить дни рождения",
            description = "Запускает ручную проверку дней рождения и отправку уведомлений"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Проверка дней рождения выполнена успешно"
    )
    public ResponseEntity<String> checkBirthdaysManually() {
        birthdayNotifyService.checkBirthdaysAndNotify();
        return ResponseEntity.ok("Проверка дней рождения выполнена");
    }
}
