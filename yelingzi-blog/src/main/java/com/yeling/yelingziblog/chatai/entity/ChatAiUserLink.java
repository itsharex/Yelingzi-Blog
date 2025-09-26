package com.yeling.yelingziblog.chatai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatAiUserLink {

    private int id;
    private String sessionId;
    private String userIdent;
    private String description;
}
