package org.example.birthdaynotifyre.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherCondition {
    @JsonProperty("text")
    private String text;
    
    @JsonProperty("icon")
    private String icon;
    
    @JsonProperty("code")
    private Integer code;
}