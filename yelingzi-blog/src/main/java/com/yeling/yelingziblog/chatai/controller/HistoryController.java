package com.yeling.yelingziblog.chatai.controller;

import com.yeling.yelingziblog.chatai.service.HistoryService;
import com.yeling.yelingziblog.common.dto.ApiResponse;
import com.yeling.yelingziblog.common.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/history")
    public ApiResponse getHistory(@RequestParam(value = "sessionId", required = false) String sessionId,
                                  @RequestHeader(value = "Authorization", required = false) String jwtToken,
                                  @RequestHeader("x-host") String deviceId) {
        return ApiResponse.success(historyService.getHistoryList(sessionId, JwtUtils.getUserIdent(jwtToken, deviceId)));
    }

    @GetMapping("/session")
    public ApiResponse getSession(@RequestHeader(value = "Authorization", required = false) String jwtToken,
                                 @RequestHeader("x-host") String deviceId) {

        return ApiResponse.success(historyService.getHistoryLink(JwtUtils.getUserIdent(jwtToken, deviceId)));
    }

    @GetMapping("/session/add")
    public ApiResponse addSession(@RequestHeader(value = "Authorization", required = false) String jwtToken,
                                  @RequestHeader("x-host") String deviceId,
                                  @RequestParam(value = "prompt") String prompt) {

        return ApiResponse.success(historyService.addHistoryLink(JwtUtils.getUserIdent(jwtToken, deviceId), prompt));
    }
}
