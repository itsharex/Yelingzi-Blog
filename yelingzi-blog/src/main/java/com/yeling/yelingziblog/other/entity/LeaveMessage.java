package com.yeling.yelingziblog.other.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveMessage {
    private Integer id;
    private String content;//内容
    private String isRead;//是否已读
    private LocalDateTime time;


}
