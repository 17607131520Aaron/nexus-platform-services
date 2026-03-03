package com.nexus.platform.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        String code,
        String messageKey,
        String message,
        T data,
        Long timestamp,
        String traceId
) {

    public static <T> ApiResponse<T> success(T data, String traceId) {
        return new ApiResponse<>(
                BaseResponseCode.SUCCESS.code(),
                BaseResponseCode.SUCCESS.messageKey(),
                BaseResponseCode.SUCCESS.defaultMessage(),
                data,
                System.currentTimeMillis(),
                traceId
        );
    }

    public static <T> ApiResponse<T> success(String message, T data, String traceId) {
        return new ApiResponse<>(
                BaseResponseCode.SUCCESS.code(),
                BaseResponseCode.SUCCESS.messageKey(),
                message,
                data,
                System.currentTimeMillis(),
                traceId
        );
    }

    public static <T> ApiResponse<T> fail(BaseResponseCode responseCode, String message, String traceId) {
        return new ApiResponse<>(
                responseCode.code(),
                responseCode.messageKey(),
                message,
                null,
                System.currentTimeMillis(),
                traceId
        );
    }

    public static <T> ApiResponse<T> fail(String code, String messageKey, String message, String traceId) {
        return new ApiResponse<>(code, messageKey, message, null, System.currentTimeMillis(), traceId);
    }
}
