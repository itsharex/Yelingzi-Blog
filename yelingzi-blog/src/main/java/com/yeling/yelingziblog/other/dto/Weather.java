package com.yeling.yelingziblog.other.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    private String guo;
    private String sheng;
    private String shi;
    private String name;
    private String weather1;
    private String weather2;
    private String wd1;
    private String wd2;
    private String winddirection1;
    private String winddirection2;
    private String windleve1;
    private String windleve2;
    private String lon;
    private String lat;
    private LocalDateTime uptime;
    private WeatherNowInfo nowinfo;


}
