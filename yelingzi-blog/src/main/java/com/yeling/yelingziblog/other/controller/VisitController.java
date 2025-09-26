package com.yeling.yelingziblog.other.controller;

import com.yeling.yelingziblog.common.dto.ApiResponse;
import com.yeling.yelingziblog.common.utils.JwtUtils;
import com.yeling.yelingziblog.other.service.VisitService;
import com.yeling.yelingziblog.common.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class VisitController {


    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    /**
     * 访问信息
     *
     */
    @GetMapping(value = "/api/view")
    public ApiResponse addViewInfo(HttpServletRequest request,
                                   @RequestHeader(value = "Authorization", required = false) String jwtToken,
                                   @RequestHeader("x-host") String deviceId){

        String ip = IpUtils.getIpAddr(request);

        visitService.recordVisit(ip, JwtUtils.getUserByTokenOrDeviceId(jwtToken, deviceId));

        return ApiResponse.success();

    }


    /**
     * 获取天气信息
     *
     */
    @GetMapping(value = "/api/weather")
    public ApiResponse getWeather(HttpServletRequest request,
                                   @RequestHeader(value = "Authorization", required = false) String jwtToken,
                                   @RequestHeader("x-host") String deviceId){

        String ip = IpUtils.getIpAddr(request);

        return ApiResponse.success(visitService.getWeather(JwtUtils.getUserIdent(jwtToken, deviceId), ip, deviceId));

    }


}
