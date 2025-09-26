package com.yeling.yelingziblog.chatai.consumer;

import com.rabbitmq.client.Channel;
import com.yeling.yelingziblog.chatai.entity.ChatAi;
import com.yeling.yelingziblog.chatai.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryMessageConsumer {
    private final HistoryService historyService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "ai.history.queue"),
            exchange = @Exchange(name = "ai.exchange"),
            key = "history.save"
    ))
    public void saveHistory(ChatAi chatRecord) {
        historyService.addHistory(chatRecord);
    }
}