package org.example.birthdaynotifyre.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.birthdaynotifyre.dto.weather.WeatherResponse;
import org.example.birthdaynotifyre.service.NotificationSender;
import org.example.birthdaynotifyre.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с сервисом WeatherService.
 */
@Slf4j
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Tag(name = "Weather API", description = "Получение информации о погоде")
public class WeatherController {

    private final WeatherService weatherService;
    private final NotificationSender notificationSender;
    private static final String CITY_TAGANROG = "Taganrog";

    @PostMapping("/notify/{chatId}")
    @Operation(
            summary = "Отправить уведомление о погоде",
            description = "Отправляет уведомление о текущей погоде в указанный чат Telegram"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Уведомление о погоде успешно отправлено"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный chatId или ошибка при отправке"
            )
    })
    public ResponseEntity<String> sendWeatherNotification(
            @Parameter(
                    description = "ID чата Telegram",
                    required = true,
                    example = "123456789"
            )
            @PathVariable String chatId,
            @Parameter(
                    description = "Город для получения погоды (опционально, по умолчанию - Taganrog)",
                    example = "Moscow"
            )
            @RequestParam(required = false, defaultValue = CITY_TAGANROG) String city) {
        try {
            String weatherInfo = weatherService.getForecastWeatherForCity(city);

            String message = String.format("🌤 Погода в %s:\n\n%s", city, weatherInfo);

            notificationSender.sendMessage(message, chatId);

            return ResponseEntity.ok("✅ Уведомление о погоде успешно отправлено в чат " + chatId);

        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления о погоде в чат {}: {}", chatId, e.getMessage());
            return ResponseEntity.badRequest().body("❌ Ошибка при отправке уведомления: " + e.getMessage());
        }
    }

    @PostMapping("/notify/morning/{chatId}")
    @Operation(
            summary = "Отправить утреннее уведомление о погоде",
            description = "Отправляет утреннее уведомление о погоде в указанный чат Telegram"
    )
    public ResponseEntity<String> sendMorningWeatherNotification(
            @PathVariable String chatId,
            @RequestParam(required = false, defaultValue = CITY_TAGANROG) String city) {
        try {
            notificationSender.sendWeatherToChat(chatId);
            return ResponseEntity.ok("✅ Утреннее уведомление о погоде отправлено в чат " + chatId);
        } catch (Exception e) {
            log.error("Ошибка при отправке утреннего уведомления в чат {}: {}", chatId, e.getMessage());
            return ResponseEntity.badRequest().body("❌ Ошибка при отправке уведомления: " + e.getMessage());
        }
    }

    @GetMapping("/current/{city}")
    @Operation(
            summary = "Получить текущую погоду",
            description = "Возвращает текущую погоду для указанного города"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о погоде успешно получены",
                    content = @Content(schema = @Schema(implementation = WeatherResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверное название города"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка при получении данных от внешнего сервиса"
            )
    })
    public ResponseEntity<WeatherResponse> getCurrentWeather(
            @Parameter(
                    description = "Название города на английском языке",
                    required = true,
                    example = "Taganrog"
            )
            @PathVariable String city) {
        WeatherResponse weatherData = weatherService.getCurrentWeather(city);
        return weatherData != null
                ? ResponseEntity.ok(weatherData)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/current/{city}/formatted")
    @Operation(
            summary = "Получить текущую погоду (форматированную)",
            description = "Возвращает отформатированную строку с текущей погодой для указанного города"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Форматированные данные о погоде успешно получены",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверное название города"
            )
    })
    public ResponseEntity<String> getFormattedWeather(
            @Parameter(
                    description = "Название города на английском языке",
                    required = true,
                    example = "Taganrog"
            )
            @PathVariable String city) {
        String formattedWeather = weatherService.getForecastWeatherForCity(city);
        return formattedWeather != null && !formattedWeather.contains("Не удалось")
                ? ResponseEntity.ok(formattedWeather)
                : ResponseEntity.badRequest().body(formattedWeather);
    }

    @GetMapping("/taganrog")
    @Operation(
            summary = "Погода в Таганроге",
            description = "Возвращает текущую погоду в Таганроге"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Данные о погоде в Таганроге успешно получены",
            content = @Content(schema = @Schema(implementation = WeatherResponse.class))
    )
    public ResponseEntity<WeatherResponse> getTaganrogWeather() {
        WeatherResponse weatherData = weatherService.getCurrentWeather(CITY_TAGANROG);
        return weatherData != null
                ? ResponseEntity.ok(weatherData)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/taganrog/formatted")
    @Operation(
            summary = "Погода в Таганроге (форматированная)",
            description = "Возвращает отформатированную строку с текущей погодой в Таганроге"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Форматированные данные о погоде в Таганроге успешно получены",
            content = @Content(schema = @Schema(implementation = String.class))
    )
    public ResponseEntity<String> getFormattedTaganrogWeather() {
        String formattedWeather = weatherService.getForecastWeatherForCity("Taganrog");
        return ResponseEntity.ok(formattedWeather);
    }
}