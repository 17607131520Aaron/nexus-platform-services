package com.nexus.platform.common.interceptor;

import com.nexus.platform.common.api.BaseResponseCode;
import com.nexus.platform.common.exception.UnauthorizedException;
import com.nexus.platform.common.security.AuthProperties;
import com.nexus.platform.common.security.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    public static final String CURRENT_USER = "CURRENT_USER";

    private final AuthProperties authProperties;
    private final JwtTokenService jwtTokenService;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AuthTokenInterceptor(AuthProperties authProperties, JwtTokenService jwtTokenService) {
        this.authProperties = authProperties;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        if (!authProperties.isEnabled() || isExcludePath(request.getRequestURI())) {
            return true;
        }

        String authorization = request.getHeader(authProperties.getHeaderName());
        String prefix = authProperties.getTokenPrefix();

        if (authorization == null || authorization.isBlank() || !authorization.startsWith(prefix)) {
            throw new UnauthorizedException(BaseResponseCode.UNAUTHORIZED.code(), "缺少有效token");
        }

        String token = authorization.substring(prefix.length()).trim();
        if (token.isEmpty()) {
            throw new UnauthorizedException(BaseResponseCode.UNAUTHORIZED.code(), "缺少有效token");
        }

        String subject = jwtTokenService.parseAndValidate(token).getSubject();
        request.setAttribute(CURRENT_USER, subject);
        return true;
    }

    private boolean isExcludePath(String path) {
        for (String pattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
