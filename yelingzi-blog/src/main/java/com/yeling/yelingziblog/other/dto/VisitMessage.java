package com.yeling.yelingziblog.other.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitMessage {

    private String ip;
    private Integer userId;
    private String nickname;
}
