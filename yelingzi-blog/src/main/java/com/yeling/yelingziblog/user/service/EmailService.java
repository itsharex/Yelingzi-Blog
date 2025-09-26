package com.yeling.yelingziblog.user.service;

import com.yeling.yelingziblog.user.dto.EmailMessage;

public interface EmailService {

    boolean sendEmail(EmailMessage emailMessage);

    void sendVerificationCodeAsync(String verifyCode, String email);

}
