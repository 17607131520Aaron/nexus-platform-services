package com.nexus.platform.common.security;

import com.nexus.platform.common.api.BaseResponseCode;
import com.nexus.platform.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenService {

    private final AuthProperties authProperties;
    private final Environment environment;
    private SecretKey signingKey;

    public JwtTokenService(AuthProperties authProperties, Environment environment) {
        this.authProperties = authProperties;
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        String secret = authProperties.getSecret();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("security.auth.secret 不能为空（建议通过环境变量JWT_SECRET配置）");
        }

        if (isPlaceholderSecret(secret) && !isLocalOrTest()) {
            throw new IllegalStateException("检测到默认JWT密钥占位符，非local/test环境禁止启动，请配置JWT_SECRET");
        }

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("security.auth.secret长度至少32个字符");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String subject) {
        Instant now = Instant.now();
        Instant expireAt = now.plus(authProperties.getExpireMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(subject)
                .issuer(authProperties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireAt))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseAndValidate(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException(BaseResponseCode.UNAUTHORIZED.code(), "token无效或已过期");
        }
    }

    private boolean isLocalOrTest() {
        for (String p : environment.getActiveProfiles()) {
            if ("local".equalsIgnoreCase(p) || "test".equalsIgnoreCase(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlaceholderSecret(String secret) {
        return "please-change-this-to-32chars-min-secret".equals(secret);
    }
}
