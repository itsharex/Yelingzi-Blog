package com.yeling.yelingziblog.other.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    private String name;
    private String intro;
    private String count;
    private String money;
    private String time;

}
