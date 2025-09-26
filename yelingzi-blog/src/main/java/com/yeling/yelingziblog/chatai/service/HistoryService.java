package com.yeling.yelingziblog.chatai.service;

import com.yeling.yelingziblog.chatai.dto.History;
import com.yeling.yelingziblog.chatai.entity.ChatAi;
import com.yeling.yelingziblog.chatai.entity.ChatAiUserLink;
import com.yeling.yelingziblog.chatai.vo.response.HistoryLinkResp;

import java.util.List;

public interface HistoryService {

    List<History> getHistory(String sessionId);

    void addHistory(ChatAi chatAi);

    void addHistoryLink(ChatAiUserLink chatAiUserLink);

    List<HistoryLinkResp> getHistoryLink(String userIdent);

    List<History> getHistoryList(String sessionId, String userIdent);

    HistoryLinkResp addHistoryLink(String userIdent, String prompt);

    long getHistoryLinkCount(String userIdent, String sessionId);
}
