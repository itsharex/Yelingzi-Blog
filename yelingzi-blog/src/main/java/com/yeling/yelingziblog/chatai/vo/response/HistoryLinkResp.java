package com.yeling.yelingziblog.chatai.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryLinkResp {

    private String sessionId;
    private String description;

}
