package com.nexus.platform.common.security;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@ConfigurationProperties(prefix = "security.auth")
public class AuthProperties {

    private boolean enabled = true;
    private String headerName = "Authorization";
    private String tokenPrefix = "Bearer ";
    private String secret = "please-change-this-to-32chars-min-secret";
    private String issuer = "nexus-platform-services";
    private long expireMinutes = 120;
    private List<String> excludePaths = new ArrayList<>(List.of("/api/auth/**", "/error"));

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setExpireMinutes(long expireMinutes) {
        this.expireMinutes = expireMinutes;
    }

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }
}
