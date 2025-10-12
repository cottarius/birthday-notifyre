package org.example.birthdaynotifyre.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.birthdaynotifyre.dto.friend.FriendDto;
import org.example.birthdaynotifyre.entity.Friend;
import org.example.birthdaynotifyre.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с сервисом FriendService.
 */
@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
@Tag(name = "Friend API", description = "Управление друзьями и днями рождения")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/create")
    @Operation(
            summary = "Создать нового друга",
            description = "Добавляет нового друга в систему для отслеживания дней рождения"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Друг успешно создан",
                    content = @Content(schema = @Schema(implementation = Friend.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные данные запроса"
            )
    })
    public ResponseEntity<Friend> createFriend(
            @Parameter(
                    description = "Данные друга",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FriendDto.class))
            )
            @RequestBody FriendDto friendDto) {
        return ResponseEntity.ok(friendService.create(friendDto));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Получить всех друзей",
            description = "Возвращает список всех друзей из системы"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список друзей успешно получен",
            content = @Content(schema = @Schema(implementation = Friend[].class))
    )
    public ResponseEntity<List<Friend>> findAll() {
        return ResponseEntity.ok(friendService.findAll());
    }

    @GetMapping("/birthdays/today")
    @Operation(
            summary = "Друзья с днем рождения сегодня",
            description = "Возвращает список друзей, у которых сегодня день рождения"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список друзей с днем рождения сегодня успешно получен",
            content = @Content(schema = @Schema(implementation = Friend[].class))
    )
    public ResponseEntity<List<Friend>> findFriendsWithBirthdayToday() {
        return ResponseEntity.ok(friendService.findFriendsWithBirthdayToday());
    }
}