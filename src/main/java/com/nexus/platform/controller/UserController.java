package com.nexus.platform.controller;

import com.nexus.platform.entity.UserEntity;
import com.nexus.platform.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserEntity getById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserEntity create(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(
                request.username(),
                request.email(),
                request.password(),
                request.realName(),
                request.phone()
        );
    }

    public record CreateUserRequest(
            @NotBlank(message = "username不能为空") String username,
            @NotBlank(message = "email不能为空") @Email(message = "email格式不正确") String email,
            @NotBlank(message = "password不能为空") String password,
            String realName,
            String phone
    ) {
    }
}
