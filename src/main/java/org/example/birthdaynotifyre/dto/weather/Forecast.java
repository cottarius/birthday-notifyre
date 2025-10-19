package org.example.birthdaynotifyre.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Прогноз погоды
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Forecast {
    /**
     * Список дней прогноза
     */
    @JsonProperty("forecastday")
    private List<ForecastDay> forecastDays;
}
