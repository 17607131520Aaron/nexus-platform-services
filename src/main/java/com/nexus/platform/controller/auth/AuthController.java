package com.nexus.platform.controller.auth;

import com.nexus.platform.common.security.AuthProperties;
import com.nexus.platform.common.security.JwtTokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenService jwtTokenService;
    private final AuthProperties authProperties;

    public AuthController(JwtTokenService jwtTokenService, AuthProperties authProperties) {
        this.jwtTokenService = jwtTokenService;
        this.authProperties = authProperties;
    }

    @PostMapping("/token")
    public TokenResponse issueToken(@Valid @RequestBody TokenRequest request) {
        String token = jwtTokenService.generateToken(request.username());
        long expiresIn = authProperties.getExpireMinutes() * 60;
        return new TokenResponse(token, "Bearer", expiresIn);
    }

    public record TokenRequest(
            @NotBlank(message = "username不能为空") String username
    ) {
    }

    public record TokenResponse(
            String token,
            String tokenType,
            Long expiresIn
    ) {
    }
}
