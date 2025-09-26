package com.yeling.yelingziblog.chatai.service.Impl;

import com.yeling.yelingziblog.chatai.dto.History;
import com.yeling.yelingziblog.chatai.entity.ChatAi;
import com.yeling.yelingziblog.chatai.entity.ChatAiUserLink;
import com.yeling.yelingziblog.chatai.mapper.HistoryMapper;
import com.yeling.yelingziblog.chatai.service.HistoryService;
import com.yeling.yelingziblog.chatai.vo.response.HistoryLinkResp;
import com.yeling.yelingziblog.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HistoryServiceImpl implements HistoryService {


    private HistoryMapper historyMapper;

    public HistoryServiceImpl(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }

    @Override
    public List<History> getHistory(String sessionId) {
        return historyMapper.selectChatAiById(sessionId);
    }

    @Override
    public void addHistory(ChatAi chatAi) {

        String content = chatAi.getContent();
        content = content.replaceAll("\n\n", "\n");
        chatAi.setContent(content);
        historyMapper.addChatAi(chatAi);
    }


    @Override
    public void addHistoryLink(ChatAiUserLink chatAiUserLink) {
        historyMapper.addChatAiUserLink(chatAiUserLink);
    }

    @Override
    public List<HistoryLinkResp> getHistoryLink(String userIdent){

        return historyMapper.selectChatAiUserLinkById(userIdent);
    }

    @Override
    public List<History> getHistoryList(String sessionId, String userIdent) {

        //查询sessionId会话是否属于userIdent
        long count = historyMapper.selectChatAiUserLinkCount(userIdent, sessionId);
        if(count == 0){
            throw new BaseException(400, "不存在会话记录:" + sessionId);
        }

        List<History> historyList = historyMapper.selectChatAiById(sessionId);
        if(historyList == null){
            throw new BaseException(400, "获取历史会话失败");
        }
        return historyList;
    }

    @Override
    public HistoryLinkResp addHistoryLink(String userIdent, String prompt) {
        //生成sessionId
        String sessionId = java.util.UUID.randomUUID().toString();
        //添加会话记录
        ChatAiUserLink chatAiUserLink = new ChatAiUserLink(0, sessionId, userIdent, prompt);
        log.info("添加会话记录:" + chatAiUserLink);
        historyMapper.addChatAiUserLink(chatAiUserLink);
        return new HistoryLinkResp(sessionId, prompt);
    }

    @Override
    public long getHistoryLinkCount(String userIdent, String sessionId){
        return historyMapper.selectChatAiUserLinkCount(userIdent, sessionId);
    }

}
