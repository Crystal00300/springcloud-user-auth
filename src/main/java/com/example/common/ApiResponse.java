package com.example.common;

import lombok.Builder;
import lombok.Data;


/**
 * 通用API返回格式，用於標準化後端響應
 *
 * @param <T>
 */
@Data
@Builder
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
