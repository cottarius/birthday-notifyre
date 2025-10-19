package org.example.birthdaynotifyre.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ответ от API погоды
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    /**
     * Информация о местоположении
     */
    @JsonProperty("location")
    private Location location;

    /**
     * Текущая погода
     */
    @JsonProperty("current")
    private CurrentWeather current;

    /**
     * Прогноз погоды
     */
    @JsonProperty("forecast")
    private Forecast forecast;
}