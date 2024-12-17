package com.example.common;

public enum ResultCode {
    SUCCESS("200", "操作成功"),
    ERROR("500", "操作失敗"),
    NOT_FOUND("404", "資源未找到"),
    UNAUTHORIZED("401", "未授權訪問"),
    FORBIDDEN("403", "禁止訪問"),
    VALIDATION_ERROR("400", "參數驗證錯誤");

    private final String code;
    private final String desc;

    ResultCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
