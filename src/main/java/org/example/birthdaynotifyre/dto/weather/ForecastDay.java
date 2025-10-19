package org.example.birthdaynotifyre.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Прогноз погоды на один день
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastDay {
    /**
     * Дата прогноза (YYYY-MM-DD)
     */
    @JsonProperty("date")
    private String date;
    
    /**
     * Дата в формате Unix timestamp
     */
    @JsonProperty("date_epoch")
    private Long dateEpoch;
    
    /**
     * Дневной прогноз
     */
    @JsonProperty("day")
    private DayForecast day;
    
    /**
     * Астрономические данные
     */
    @JsonProperty("astro")
    private Astro astro;
    
    /**
     * Почасовой прогноз
     */
    @JsonProperty("hour")
    private List<HourForecast> hour;
}