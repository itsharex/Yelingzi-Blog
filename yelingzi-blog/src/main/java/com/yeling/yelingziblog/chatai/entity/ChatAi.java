package com.yeling.yelingziblog.chatai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatAi {

    private int id;
    private String sessionId;
    private String role;
    private String content;
    private LocalDateTime dateTime;

}
