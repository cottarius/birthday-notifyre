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
public class Location {
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("region")
    private String region;
    
    @JsonProperty("country")
    private String country;
    
    @JsonProperty("lat")
    private Double lat;
    
    @JsonProperty("lon")
    private Double lon;
    
    @JsonProperty("tz_id")
    private String timezoneId;
    
    @JsonProperty("localtime_epoch")
    private Long localtimeEpoch;
    
    @JsonProperty("localtime")
    private String localtime;
}