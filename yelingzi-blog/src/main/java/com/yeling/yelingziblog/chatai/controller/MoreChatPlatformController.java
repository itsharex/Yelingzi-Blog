package com.yeling.yelingziblog.chatai.controller;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/ai")
public class MoreChatPlatformController {

    HashMap<String, ChatModel> platforms = new HashMap<>();


    public MoreChatPlatformController(
            DashScopeChatModel dashScopeChatModel
    ) {
        platforms.put("dashscope", dashScopeChatModel);
    }



//
//    @GetMapping(value ="/chat", produces = "text/stream;charset=UTF-8")
//    public Flux<String> generation(@RequestParam("prompt") String userInput,
//                                   @Autowired ChatClient.Builder chatClientBuilder) {
//
//        ChatClient client = chatClientBuilder
//                .defaultSystem(
//                        //系统提示模板
//                                """
//                                #角色说明
//
//
//                                当前回复用户：{name}
//
//                                """
//                ).defaultAdvisors(
//                        //日志
//                        new SimpleLoggerAdvisor(),
//                        //敏感词
//                        new SafeGuardAdvisor(List.of("111")
//                        )
//                ).build();
//
//        return client.prompt()
//                .system(p -> p.param("name", "张三"))
//                .user(userInput)
//                .stream()
//                .content();
//    }




}
