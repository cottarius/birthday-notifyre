package org.example.birthdaynotifyre.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Почасовой прогноз погоды
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HourForecast {
    /**
     * Время в формате Unix timestamp
     */
    @JsonProperty("time_epoch")
    private Long timeEpoch;
    
    /**
     * Время (YYYY-MM-DD HH:MM)
     */
    @JsonProperty("time")
    private String time;
    
    /**
     * Температура в °C
     */
    @JsonProperty("temp_c")
    private Double tempC;
    
    /**
     * Температура в °F
     */
    @JsonProperty("temp_f")
    private Double tempF;
    
    /**
     * День или ночь (1 - день, 0 - ночь)
     */
    @JsonProperty("is_day")
    private Integer isDay;
    
    /**
     * Условия погоды
     */
    @JsonProperty("condition")
    private WeatherCondition condition;
    
    /**
     * Скорость ветра в mph
     */
    @JsonProperty("wind_mph")
    private Double windMph;
    
    /**
     * Скорость ветра в km/h
     */
    @JsonProperty("wind_kph")
    private Double windKph;
    
    /**
     * Направление ветра в градусах
     */
    @JsonProperty("wind_degree")
    private Integer windDegree;
    
    /**
     * Направление ветра (N, NE, E, SE, S, SW, W, NW)
     */
    @JsonProperty("wind_dir")
    private String windDir;
    
    /**
     * Давление в mb
     */
    @JsonProperty("pressure_mb")
    private Double pressureMb;
    
    /**
     * Давление в дюймах ртутного столба
     */
    @JsonProperty("pressure_in")
    private Double pressureIn;
    
    /**
     * Осадки в мм
     */
    @JsonProperty("precip_mm")
    private Double precipMm;
    
    /**
     * Осадки в дюймах
     */
    @JsonProperty("precip_in")
    private Double precipIn;
    
    /**
     * Снег в см
     */
    @JsonProperty("snow_cm")
    private Double snowCm;
    
    /**
     * Влажность в %
     */
    @JsonProperty("humidity")
    private Integer humidity;
    
    /**
     * Облачность в %
     */
    @JsonProperty("cloud")
    private Integer cloud;
    
    /**
     * Ощущаемая температура в °C
     */
    @JsonProperty("feelslike_c")
    private Double feelslikeC;
    
    /**
     * Ощущаемая температура в °F
     */
    @JsonProperty("feelslike_f")
    private Double feelslikeF;
    
    /**
     * Температура по ветро-холодовому индексу в °C
     */
    @JsonProperty("windchill_c")
    private Double windchillC;
    
    /**
     * Температура по ветро-холодовому индексу в °F
     */
    @JsonProperty("windchill_f")
    private Double windchillF;
    
    /**
     * Температура по heat index в °C
     */
    @JsonProperty("heatindex_c")
    private Double heatindexC;
    
    /**
     * Температура по heat index в °F
     */
    @JsonProperty("heatindex_f")
    private Double heatindexF;
    
    /**
     * Точка росы в °C
     */
    @JsonProperty("dewpoint_c")
    private Double dewpointC;
    
    /**
     * Точка росы в °F
     */
    @JsonProperty("dewpoint_f")
    private Double dewpointF;
    
    /**
     * Будет ли дождь (1 - да, 0 - нет)
     */
    @JsonProperty("will_it_rain")
    private Integer willItRain;
    
    /**
     * Вероятность дождя в %
     */
    @JsonProperty("chance_of_rain")
    private Integer chanceOfRain;
    
    /**
     * Будет ли снег (1 - да, 0 - нет)
     */
    @JsonProperty("will_it_snow")
    private Integer willItSnow;
    
    /**
     * Вероятность снега в %
     */
    @JsonProperty("chance_of_snow")
    private Integer chanceOfSnow;
    
    /**
     * Видимость в км
     */
    @JsonProperty("vis_km")
    private Double visKm;
    
    /**
     * Видимость в милях
     */
    @JsonProperty("vis_miles")
    private Double visMiles;
    
    /**
     * Порывы ветра в mph
     */
    @JsonProperty("gust_mph")
    private Double gustMph;
    
    /**
     * Порывы ветра в km/h
     */
    @JsonProperty("gust_kph")
    private Double gustKph;
    
    /**
     * УФ-индекс
     */
    @JsonProperty("uv")
    private Double uv;
    
    /**
     * Коротковолновая радиация
     */
    @JsonProperty("short_rad")
    private Double shortRad;
    
    /**
     * Диффузная радиация
     */
    @JsonProperty("diff_rad")
    private Double diffRad;
    
    /**
     * Прямая нормальная irradiance
     */
    @JsonProperty("dni")
    private Double dni;
    
    /**
     * Глобальная tilted irradiance
     */
    @JsonProperty("gti")
    private Double gti;
}