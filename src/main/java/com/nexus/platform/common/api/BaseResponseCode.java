package com.nexus.platform.common.api;

public enum BaseResponseCode {
    SUCCESS("0", "success", "成功"),
    BAD_REQUEST("A0400", "bad_request", "请求参数错误"),
    NOT_FOUND("A0404", "not_found", "资源不存在"),
    METHOD_NOT_ALLOWED("A0405", "method_not_allowed", "请求方法不支持"),
    INTERNAL_ERROR("B0500", "internal_error", "服务器内部错误");

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    BaseResponseCode(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    public String code() {
        return code;
    }

    public String messageKey() {
        return messageKey;
    }

    public String defaultMessage() {
        return defaultMessage;
    }
}
