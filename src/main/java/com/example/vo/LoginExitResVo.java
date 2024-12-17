package com.example.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginExitResVo {
    private String userId;
    private String accessToken;
}
