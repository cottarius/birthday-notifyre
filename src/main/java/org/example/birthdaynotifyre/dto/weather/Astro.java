package org.example.birthdaynotifyre.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Астрономические данные
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Astro {
    /**
     * Время восхода солнца
     */
    @JsonProperty("sunrise")
    private String sunrise;
    
    /**
     * Время заката солнца
     */
    @JsonProperty("sunset")
    private String sunset;
    
    /**
     * Время восхода луны
     */
    @JsonProperty("moonrise")
    private String moonrise;
    
    /**
     * Время заката луны
     */
    @JsonProperty("moonset")
    private String moonset;
    
    /**
     * Фаза луны
     */
    @JsonProperty("moon_phase")
    private String moonPhase;
    
    /**
     * Освещенность луны в %
     */
    @JsonProperty("moon_illumination")
    private Integer moonIllumination;
    
    /**
     * Находится ли луна над горизонтом (1 - да, 0 - нет)
     */
    @JsonProperty("is_moon_up")
    private Integer isMoonUp;
    
    /**
     * Находится ли солнце над горизонтом (1 - да, 0 - нет)
     */
    @JsonProperty("is_sun_up")
    private Integer isSunUp;
}