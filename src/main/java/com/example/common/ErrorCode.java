package com.example.common;

public enum ErrorCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失敗"),
    INVALID_SMS_CODE(1001, "驗證碼輸入錯誤"),
    USER_NOT_FOUND(1002, "用戶不存在"),
    UNAUTHORIZED_ACCESS(1003, "未授權訪問");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
