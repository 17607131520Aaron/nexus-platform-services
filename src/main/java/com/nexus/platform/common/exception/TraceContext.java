package com.nexus.platform.common.exception;

import jakarta.servlet.http.HttpServletRequest;

public final class TraceContext {

    public static final String TRACE_ID = "TRACE_ID";

    private TraceContext() {
    }

    public static String getTraceId(HttpServletRequest request) {
        Object value = request.getAttribute(TRACE_ID);
        return value == null ? null : value.toString();
    }
}
