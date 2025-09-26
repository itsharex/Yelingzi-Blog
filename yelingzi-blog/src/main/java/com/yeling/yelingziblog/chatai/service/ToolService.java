package com.yeling.yelingziblog.chatai.service;

import com.yeling.yelingziblog.user.entity.User;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;

public interface ToolService  {

    String addFriendLink(String title, String cover, String introduction, String url, User user);

    String addMessage(String content, User user);

    List<String> getHotArticle();

    String getArticleContent(Integer id);

    List<ToolCallback> getToolList(ToolService toolService);

}
