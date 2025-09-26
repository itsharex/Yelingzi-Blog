package com.yeling.yelingziblog.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageSendDto {

    private String toGroup;

    private String type;

    private String message;


}
