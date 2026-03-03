package com.nexus.platform.common.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.platform.common.exception.TraceContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public GlobalResponseAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body instanceof ApiResponse<?>) {
            return body;
        }

        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return ApiResponse.success(body, null);
        }

        HttpServletRequest rawRequest = servletRequest.getServletRequest();
        String traceId = TraceContext.getTraceId(rawRequest);
        ApiResponse<Object> wrapped = ApiResponse.success(body, traceId);

        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(wrapped);
            } catch (JsonProcessingException e) {
                return "{\"code\":\"B0500\",\"messageKey\":\"internal_error\",\"message\":\"响应序列化失败\"}";
            }
        }

        return wrapped;
    }
}
