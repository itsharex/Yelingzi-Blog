package com.yeling.yelingziblog.common.exception;

public class EmailSendFailureException extends BaseException {
    public EmailSendFailureException() {
        super("发送邮件失败，请稍后重试");
    }
}
