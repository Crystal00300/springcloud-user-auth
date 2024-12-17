package com.example.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserSmsCode {
    private Long id;
    private String mobileNo; // 使用者註冊手機號碼
    private String smsCode; // 簡訊驗證碼
    private Timestamp sendTime; // 發送時間
    private Timestamp createTime; // 建立時間
}
