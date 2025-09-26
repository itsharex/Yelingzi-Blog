package com.yeling.yelingziblog.chat.service;

import com.yeling.yelingziblog.chat.dto.NewMessageCountDto;
import com.yeling.yelingziblog.chat.vo.request.MessageListReq;
import com.yeling.yelingziblog.chat.vo.request.SendMessageReq;
import com.yeling.yelingziblog.chat.vo.response.ChatMessageResp;
import com.yeling.yelingziblog.chat.vo.response.MessageListResp;
import com.yeling.yelingziblog.chat.vo.response.NewGroupChatBriefResp;
import com.yeling.yelingziblog.chat.vo.response.NewSingleChatBriefResp;
import com.yeling.yelingziblog.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {

    void sendMessage(User user, SendMessageReq sendMessageReq, String ip);

    void sendEmojiMessage(User user, SendMessageReq sendMessageReq, String ip);


    void sendImageMessage(User user, MultipartFile multipartFile, String ip);

    ChatMessageResp adminSendSingleMessage(User user, String message, String ip, String toUser);


    MessageListResp getMessageList(MessageListReq messageListReq, User user);

    List<NewSingleChatBriefResp> getNewSingleChatCount(NewMessageCountDto newMessageCountDto);

    List<NewGroupChatBriefResp> getNewGroupChatCount(int cursor);

}
