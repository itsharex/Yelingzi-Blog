package com.yeling.yelingziblog.chatai.vo.request;

import com.yeling.yelingziblog.chatai.dto.MorePlatformModelOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreChatReq {

    private MorePlatformModelOptions options;

    private String prompt;
    private String sessionId;
}
