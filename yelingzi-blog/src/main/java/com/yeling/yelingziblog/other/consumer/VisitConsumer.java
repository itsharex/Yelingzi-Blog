package com.yeling.yelingziblog.other.consumer;

import com.yeling.yelingziblog.other.dto.VisitMessage;
import com.yeling.yelingziblog.other.service.VisitService;
import com.yeling.yelingziblog.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VisitConsumer {


    private final VisitService visitService;

    public VisitConsumer(VisitService visitService) {
        this.visitService = visitService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "visit.queue"),
            exchange = @Exchange(name = "other.exchange"),
            key = "other.visit"
    ))
    public void handle(VisitMessage msg) {
        try {
            log.info("[Consumer] 收到消息，ip={}, user={}", msg.getIp(), msg.getUserId());
            // 构造 User 对象
            User user = new User();
            user.setId(msg.getUserId());
            user.setNickname(msg.getNickname());

            // 直接复用之前写好的 addVisitInfo 方法（天气、Redis、计数器都在里面）
            visitService.addVisitInfo(msg.getIp(), user);
        } catch (Exception e) {
            log.error("[Consumer] 消费失败，msg=" + msg, e);
            // 也可以手动 basicNack 重试，这里简单记录
        }
    }
}
