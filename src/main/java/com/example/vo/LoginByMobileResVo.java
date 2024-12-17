package com.example.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LoginByMobileResVo implements Serializable {
    private String userId;
    private String accessToken;

}

