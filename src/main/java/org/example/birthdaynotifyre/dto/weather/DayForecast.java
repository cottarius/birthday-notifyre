package org.example.birthdaynotifyre.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Дневной прогноз погоды
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayForecast {
    /**
     * Максимальная температура в °C
     */
    @JsonProperty("maxtemp_c")
    private Double maxTempC;
    
    /**
     * Максимальная температура в °F
     */
    @JsonProperty("maxtemp_f")
    private Double maxTempF;
    
    /**
     * Минимальная температура в °C
     */
    @JsonProperty("mintemp_c")
    private Double minTempC;
    
    /**
     * Минимальная температура в °F
     */
    @JsonProperty("mintemp_f")
    private Double minTempF;
    
    /**
     * Средняя температура в °C
     */
    @JsonProperty("avgtemp_c")
    private Double avgTempC;
    
    /**
     * Средняя температура в °F
     */
    @JsonProperty("avgtemp_f")
    private Double avgTempF;
    
    /**
     * Максимальная скорость ветра в mph
     */
    @JsonProperty("maxwind_mph")
    private Double maxWindMph;
    
    /**
     * Максимальная скорость ветра в km/h
     */
    @JsonProperty("maxwind_kph")
    private Double maxWindKph;
    
    /**
     * Сумма осадков в мм
     */
    @JsonProperty("totalprecip_mm")
    private Double totalPrecipMm;
    
    /**
     * Сумма осадков в дюймах
     */
    @JsonProperty("totalprecip_in")
    private Double totalPrecipIn;
    
    /**
     * Сумма снега в см
     */
    @JsonProperty("totalsnow_cm")
    private Double totalSnowCm;
    
    /**
     * Средняя видимость в км
     */
    @JsonProperty("avgvis_km")
    private Double avgVisKm;
    
    /**
     * Средняя видимость в милях
     */
    @JsonProperty("avgvis_miles")
    private Double avgVisMiles;
    
    /**
     * Средняя влажность в %
     */
    @JsonProperty("avghumidity")
    private Integer avgHumidity;
    
    /**
     * Будет ли дождь (1 - да, 0 - нет)
     */
    @JsonProperty("daily_will_it_rain")
    private Integer dailyWillItRain;
    
    /**
     * Вероятность дождя в %
     */
    @JsonProperty("daily_chance_of_rain")
    private Integer dailyChanceOfRain;
    
    /**
     * Будет ли снег (1 - да, 0 - нет)
     */
    @JsonProperty("daily_will_it_snow")
    private Integer dailyWillItSnow;
    
    /**
     * Вероятность снега в %
     */
    @JsonProperty("daily_chance_of_snow")
    private Integer dailyChanceOfSnow;
    
    /**
     * Условия погоды
     */
    @JsonProperty("condition")
    private WeatherCondition condition;
    
    /**
     * УФ-индекс
     */
    @JsonProperty("uv")
    private Double uv;
}