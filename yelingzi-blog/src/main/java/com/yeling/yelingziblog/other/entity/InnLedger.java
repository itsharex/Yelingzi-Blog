package com.yeling.yelingziblog.other.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InnLedger {

    private Integer id;
    private String roomName;
    private String startTime;
    private String endTime;
    private String orderNum;
    private String state;
    private String intro;
    private String room;
}
