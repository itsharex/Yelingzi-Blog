package com.yeling.yelingziblog.other.service;

import com.yeling.yelingziblog.other.entity.Message;
import com.yeling.yelingziblog.common.dto.PageResult;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.other.vo.request.MessageReq;
import com.yeling.yelingziblog.other.vo.response.MessageResp;

import java.util.List;

public interface MessageService {

    void addMessage(String messageReq, User user);

    void delMessage(Long id);

    void updateMessage(Long id);

    PageResult<Message> getMessageListByPage(int page, int pageSize);

    PageResult<Message> userGetMessageListByPage(int page, int pageSize, Integer userId);

    List<MessageResp> getMessageList();

}
