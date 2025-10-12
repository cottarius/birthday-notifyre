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
public class CurrentWeather {
    @JsonProperty("last_updated_epoch")
    private Long lastUpdatedEpoch;
    
    @JsonProperty("last_updated")
    private String lastUpdated;
    
    @JsonProperty("temp_c")
    private Double tempC;
    
    @JsonProperty("temp_f")
    private Double tempF;
    
    @JsonProperty("is_day")
    private Integer isDay;
    
    @JsonProperty("condition")
    private WeatherCondition condition;
    
    @JsonProperty("wind_mph")
    private Double windMph;
    
    @JsonProperty("wind_kph")
    private Double windKph;
    
    @JsonProperty("wind_degree")
    private Integer windDegree;
    
    @JsonProperty("wind_dir")
    private String windDir;
    
    @JsonProperty("pressure_mb")
    private Double pressureMb;
    
    @JsonProperty("pressure_in")
    private Double pressureIn;
    
    @JsonProperty("precip_mm")
    private Double precipMm;
    
    @JsonProperty("precip_in")
    private Double precipIn;
    
    @JsonProperty("humidity")
    private Integer humidity;
    
    @JsonProperty("cloud")
    private Integer cloud;
    
    @JsonProperty("feelslike_c")
    private Double feelslikeC;
    
    @JsonProperty("feelslike_f")
    private Double feelslikeF;
    
    @JsonProperty("windchill_c")
    private Double windchillC;
    
    @JsonProperty("windchill_f")
    private Double windchillF;
    
    @JsonProperty("heatindex_c")
    private Double heatindexC;
    
    @JsonProperty("heatindex_f")
    private Double heatindexF;
    
    @JsonProperty("dewpoint_c")
    private Double dewpointC;
    
    @JsonProperty("dewpoint_f")
    private Double dewpointF;
    
    @JsonProperty("vis_km")
    private Double visKm;
    
    @JsonProperty("vis_miles")
    private Double visMiles;
    
    @JsonProperty("uv")
    private Double uv;
    
    @JsonProperty("gust_mph")
    private Double gustMph;
    
    @JsonProperty("gust_kph")
    private Double gustKph;
}