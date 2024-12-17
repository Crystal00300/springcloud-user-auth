package com.example.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginExitReqVo {
    private String userId;
    private String accessToken; // 用於標識登出會話的令牌
}
