package org.example.birthdaynotifyre.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.birthdaynotifyre.dto.weather.CurrentWeather;
import org.example.birthdaynotifyre.dto.weather.Location;
import org.example.birthdaynotifyre.dto.weather.WeatherCondition;
import org.example.birthdaynotifyre.dto.weather.WeatherResponse;
import org.example.birthdaynotifyre.exception.ParseWeatherException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.example.birthdaynotifyre.common.ExceptionMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    @Value("${weather.api.key}")
    private String apiKey;

    public String getForecastWeatherForCity(String city) {
        try {
            String url = String.format("http://api.weatherapi.com/v1/forecast.json?key=%s&q=%s&days=1", apiKey, city);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error(REQUEST_WEATHER_EXCEPTION, response.statusCode());
                return "Не удалось получить данные о погоде. Попробуйте позже.";
            }

            return parseWeatherResponse(response.body());
        } catch (Exception e) {
            log.error(GET_WEATHER_FOR_CITY_EXCEPTION, city, e.getMessage());
            return "Произошла ошибка при получении погоды. Попробуйте позже.";
        }
    }

    private String parseWeatherResponse(String jsonData) {
        WeatherResponse weatherResponse;
        try {
            weatherResponse = objectMapper.readValue(jsonData, WeatherResponse.class);
            log.info("Данные о погоде успешно прочитаны");
        } catch (Exception e) {
            log.error("Ошибка при парсинге ответа погоды: {}", e.getMessage());
            return "Не удалось обработать данные о погоде.";
        }

        if (weatherResponse == null) {
            return "Не удалось обработать данные о погоде.";
        }

        String location = Optional.ofNullable(weatherResponse.getLocation())
                .map(Location::getName)
                .orElseThrow(() -> new ParseWeatherException(PARSE_WEATHER_EXCEPTION));

        Double tempC = Optional.ofNullable(weatherResponse.getCurrent())
                .map(CurrentWeather::getTempC)
                .orElseThrow(() -> new ParseWeatherException(PARSE_WEATHER_EXCEPTION));

        String condition = Optional.ofNullable(weatherResponse.getCurrent())
                .map(CurrentWeather::getCondition)
                .map(WeatherCondition::getText)
                .orElseThrow(() -> new ParseWeatherException(PARSE_WEATHER_EXCEPTION));

        Double feelsLikeC = Optional.ofNullable(weatherResponse.getCurrent())
                .map(CurrentWeather::getFeelslikeC)
                .orElseThrow(() -> new ParseWeatherException(PARSE_WEATHER_EXCEPTION));

        Integer humidity = Optional.ofNullable(weatherResponse.getCurrent())
                .map(CurrentWeather::getHumidity)
                .orElseThrow(() -> new ParseWeatherException(PARSE_WEATHER_EXCEPTION));

        Double windKph = Optional.ofNullable(weatherResponse.getCurrent())
                .map(CurrentWeather::getWindKph)
                .orElseThrow(() -> new ParseWeatherException(PARSE_WEATHER_EXCEPTION));

//        Optional.ofNullable(weatherResponse.getForecast())
//                .map(Forecast::getForecastDays)
//                .flatMap(List::stream)
//                .map()
        Double maxTempC = weatherResponse.getForecast().getForecastDays().stream().findFirst().get().getDay().getMaxTempC();
        Double minTempC = weatherResponse.getForecast().getForecastDays().stream().findFirst().get().getDay().getMinTempC();
        Double maxWindKph = weatherResponse.getForecast().getForecastDays().stream().findFirst().get().getDay().getMaxWindKph();
        Integer dailyChanceOfRain = weatherResponse.getForecast().getForecastDays().stream().findFirst().get().getDay().getDailyChanceOfRain();
        Integer dailyChanceOfSnow = weatherResponse.getForecast().getForecastDays().stream().findFirst().get().getDay().getDailyChanceOfSnow();
        String dayCondition = weatherResponse.getForecast().getForecastDays().stream().findFirst().get().getDay().getCondition().getText();

        return weatherDataToString(location, tempC, feelsLikeC, condition, humidity, windKph, maxTempC, minTempC, maxWindKph, dailyChanceOfRain, dailyChanceOfSnow, dayCondition);
    }

    private static String weatherDataToString(String location, Double tempC, Double feelsLikeC, String condition, Integer humidity, Double windKph, Double maxTempC, Double minTempC, Double maxWindKph, Integer dailyChanceOfRain, Integer dailyChanceOfSnow, String dayCondition) {
        return String.format("""
                        🌤 Сейчас погода в %s:
                        🌡 Температура: %.1f°C
                        💭 Ощущается как: %.1f°C
                        ☁️ Состояние: %s
                        💧 Влажность: %d%%
                        💨 Ветер: %.1f км/ч
                        
                        В течение дня ожидается погода:
                        🌡 max температура: %.1f°C
                        🌡 min температура: %.1f°C
                        💨 max скорость ветра: %.1f км/ч
                        ☁️ Вероятность дождя: %d%%
                        ☁️ Вероятность снега: %d%%
                        ☁️ Состояние: %s
                        """,
                location, tempC, feelsLikeC, condition, humidity, windKph, maxTempC, minTempC, maxWindKph,
                dailyChanceOfRain, dailyChanceOfSnow, dayCondition
        );
    }

    public WeatherResponse getCurrentWeather(String city) {
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
                log.error(REQUEST_WEATHER_EXCEPTION, response.statusCode());
                return null;
            }

        } catch (Exception e) {
            log.error(GET_WEATHER_FOR_CITY_EXCEPTION, city, e.getMessage());
            return null;
        }
    }
}
