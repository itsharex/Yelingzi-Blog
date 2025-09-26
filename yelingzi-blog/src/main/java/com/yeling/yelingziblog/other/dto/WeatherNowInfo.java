package com.yeling.yelingziblog.other.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherNowInfo {

    private String precipitation;
    private String temperature;
    private String pressure;
    private String humidity;
    private String windDirection;
    private String windDirectionDegree;
    private String windSpeed;
    private String windScale;
    private String feelst;
    private String uptime;

}
