package com.yeling.yelingziblog.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {

    private T data;

    private String errorCode;

    private String errorMessage;

    private String debugMessage;

}
