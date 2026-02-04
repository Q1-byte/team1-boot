package com.example.jpa.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private int code;
    private String message;
    private T data;
    
    // 성공 응답 (데이터 있음)
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("성공")
                .data(data)
                .build();
    }
    
    // 성공 응답 (메시지 + 데이터)
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }
    
    // 성공 응답 (데이터 없음)
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message(message)
                .data(null)
                .build();
    }
    
    // 생성 성공 (201)
    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("생성 완료")
                .data(data)
                .build();
    }
    
    // 실패 응답
    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(status.value())
                .message(message)
                .data(null)
                .build();
    }
    
    // 400 Bad Request
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message);
    }
    
    // 401 Unauthorized
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED, message);
    }
    
    // 404 Not Found
    public static <T> ApiResponse<T> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message);
    }
    
    // 500 Internal Server Error
    public static <T> ApiResponse<T> serverError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
