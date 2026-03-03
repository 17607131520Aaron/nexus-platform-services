package com.nexus.platform.common.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    private final int code;

    public UnauthorizedException(int code, String message) {
        super(message);
        this.code = code;
    }

}
