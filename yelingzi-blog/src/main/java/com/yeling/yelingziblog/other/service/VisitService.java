package com.yeling.yelingziblog.other.service;

import com.yeling.yelingziblog.common.vo.response.WeatherResp;
import com.yeling.yelingziblog.other.dto.Weather;
import com.yeling.yelingziblog.user.entity.User;

public interface VisitService {

    void recordVisit(String ip, User user);

    Weather getWeather(String userIdent, String ip, String deviceId);

    void addVisitInfo(String ip, User user);

}
