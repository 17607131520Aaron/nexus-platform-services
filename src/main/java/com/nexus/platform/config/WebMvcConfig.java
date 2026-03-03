package com.nexus.platform.config;

import com.nexus.platform.common.interceptor.RequestTraceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestTraceInterceptor requestTraceInterceptor;

    public WebMvcConfig(RequestTraceInterceptor requestTraceInterceptor) {
        this.requestTraceInterceptor = requestTraceInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTraceInterceptor).addPathPatterns("/**");
    }
}
