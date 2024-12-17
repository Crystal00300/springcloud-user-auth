package com.example.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本ID
    private Long id;
    private String userId; // 使用者ID
    private String nickName; // 使用者暱稱
    private String mobileNo; // 使用者註冊手機號碼
    private String password; // 登入密碼
    private Integer isLogin; // 是否登入（0：未登入/ 1：已登入 ）
    private Timestamp loginTime; // 最近登入時間
    private Integer isDel; // 是否登出（0：未登出/ 1：已登出 ）
    private Timestamp createTime; // 建立時間
}
