package com.yeling.yelingziblog.common.exception;

public class EmailNotRegisteredException extends BaseException {
    public EmailNotRegisteredException() {
        super("邮箱未注册");
    }
}
