package org.example.birthdaynotifyre.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.birthdaynotifyre.dto.weather.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    @Value("${weather.api.key}")
    private String apiKey;

    public String getWeatherForCity(String city) {
        try {
            String url = String.format("http://api.weatherapi.com/v1/current.json?key=%s&q=%s", apiKey, city);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Ошибка при запросе погоды. Код ответа: {}", response.statusCode());
                return "Не удалось получить данные о погоде. Попробуйте позже.";
            }

            return parseWeatherResponse(response.body());
        } catch (Exception e) {
            log.error("Ошибка при получении погоды для города {}: {}", city, e.getMessage());
            return "Произошла ошибка при получении погоды. Попробуйте позже.";
        }
    }

    private String parseWeatherResponse(String jsonData) {
        try {
            WeatherResponse weatherResponse = objectMapper.readValue(jsonData, WeatherResponse.class);

            String location = weatherResponse.getLocation().getName();
            Double tempC = weatherResponse.getCurrent().getTempC();
            Double feelsLikeC = weatherResponse.getCurrent().getFeelslikeC();
            String condition = weatherResponse.getCurrent().getCondition().getText();
            Integer humidity = weatherResponse.getCurrent().getHumidity();
            Double windKph = weatherResponse.getCurrent().getWindKph();

            return String.format("🌤 Погода в %s:\n" +
                            "🌡 Температура: %.1f°C\n" +
                            "💭 Ощущается как: %.1f°C\n" +
                            "☁️  Состояние: %s\n" +
                            "💧 Влажность: %d%%\n" +
                            "💨 Ветер: %.1f км/ч",
                    location, tempC, feelsLikeC, condition, humidity, windKph);

        } catch (Exception e) {
            log.error("Ошибка при парсинге ответа погоды: {}", e.getMessage());
            return "Не удалось обработать данные о погоде.";
        }
    }

    public WeatherResponse getWeatherData(String city) {
        try {
            String url = String.format("http://api.weatherapi.com/v1/current.json?key=%s&q=%s", apiKey, city);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), WeatherResponse.class);
            } else {
                log.error("Ошибка при запросе погоды. Код ответа: {}", response.statusCode());
                return null;
            }

        } catch (Exception e) {
            log.error("Ошибка при получении погоды для города {}: {}", city, e.getMessage());
            return null;
        }
    }
}
