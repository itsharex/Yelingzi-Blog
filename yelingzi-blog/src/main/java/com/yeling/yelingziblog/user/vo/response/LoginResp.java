package com.yeling.yelingziblog.user.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResp {

    private String accessToken;

    private String refreshToken;

}
