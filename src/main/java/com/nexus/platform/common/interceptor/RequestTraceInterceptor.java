package com.nexus.platform.common.interceptor;

import com.nexus.platform.common.exception.TraceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class RequestTraceInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestTraceInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        request.setAttribute(TraceContext.TRACE_ID, traceId);
        response.setHeader("X-Trace-Id", traceId);
        log.info("[{}] {} {}", traceId, request.getMethod(), request.getRequestURI());
        return true;
    }
}
