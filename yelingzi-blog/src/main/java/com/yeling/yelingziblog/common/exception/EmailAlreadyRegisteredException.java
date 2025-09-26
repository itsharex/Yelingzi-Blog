package com.yeling.yelingziblog.common.exception;

public class EmailAlreadyRegisteredException extends BaseException {
    public EmailAlreadyRegisteredException() {
        super("邮箱已被注册");
    }
}