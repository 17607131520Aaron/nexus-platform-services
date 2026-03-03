package com.nexus.platform.common.api;

public record ApiResponse<T>(
        int code,
        T data,
        String message
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                BaseResponseCode.SUCCESS.code(),
                data,
                BaseResponseCode.SUCCESS.defaultMessage()
        );
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                BaseResponseCode.SUCCESS.code(),
                data,
                message
        );
    }

    public static <T> ApiResponse<T> fail(BaseResponseCode responseCode, String message) {
        return new ApiResponse<>(
                responseCode.code(),
                null,
                message
        );
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, null, message);
    }
}
