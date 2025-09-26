package com.yeling.yelingziblog.chatai.service.Impl;

import com.yeling.yelingziblog.article.service.ArticleService;
import com.yeling.yelingziblog.article.vo.response.ArticleResp;
import com.yeling.yelingziblog.chatai.service.ToolService;
import com.yeling.yelingziblog.other.entity.Friend;
import com.yeling.yelingziblog.other.service.FriendService;
import com.yeling.yelingziblog.other.service.MessageService;
import com.yeling.yelingziblog.other.vo.request.FriendReq;
import com.yeling.yelingziblog.user.entity.User;
import org.apache.el.util.ReflectionUtil;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToolServiceImpl implements ToolService {

    private final FriendService friendService;

    private final MessageService messageService;

    private final ArticleService articleService;


    public ToolServiceImpl(FriendService friendService,
                           MessageService messageService,
                           ArticleService articleService) {
        this.friendService = friendService;
        this.messageService = messageService;
        this.articleService = articleService;
    }

    @Override
    @Tool(description = "添加友链")
    public String addFriendLink(@ToolParam(description = "网站的标题") String title,
                                @ToolParam(description = "网站的封面链接，http开头") String cover,
                                @ToolParam(description = "网站的简介") String introduction,
                                @ToolParam(description = "网站的链接，http开头") String url,
                                @ToolParam(description = "用户信息") User user) {

        friendService.addFriend(new FriendReq(title, cover, introduction,url), user);

        return "友链添加成功";
    }

    @Override
    @Tool(description = "留言板留言")
    public String addMessage(@ToolParam(description = "留言内容") String content,
                             @ToolParam(description = "用户信息") User user) {

        messageService.addMessage(content, user);

        return "留言成功";
    }

    @Override
    @Tool(description = "查看热门文章列表")
    public List<String> getHotArticle() {
        List<ArticleResp> recommendArticleList = articleService.getRecommendArticleList();

        return recommendArticleList.stream()
                .map(articleResp -> "('" + articleResp.getTitle() + "')['https://www.yeling.top/article/" + articleResp.getId() + "']")
                .collect(Collectors.toList());
    }

    @Override
    @Tool(description = "根据文章Id获取文章内容")
    public String getArticleContent(@ToolParam(description = "文章id") Integer id) {
        ArticleResp articleResp = articleService.findArticleById(id);

        return "文章内容：" + articleResp.getContent();
    }

    @Override
    public List<ToolCallback> getToolList(ToolService toolService) {

        Method method = ReflectionUtils.findMethod(ToolServiceImpl.class, "addFriendLink", String.class, String.class);

        ToolDefinition toolDefinition = ToolDefinition.builder()
                .name("addFriendLink")
                .description("添加友链")
                .inputSchema("""
                        {
                            "type": "object",
                            "properties":{
                                "title":{
                                    "type":"string",
                                    "description":"网站标题"
                                },
                                "url":{
                                    "type":"string",
                                    "description":"网站链接"
                                }
                            }
                            "required":["title","url"]
                        }
                        """)
                .build();

        ToolCallback toolCallback = MethodToolCallback.builder()
                .toolDefinition(toolDefinition)
                .toolMethod(method)
                .toolObject(toolService)
                .build();

        return List.of(toolCallback);
    }


}
