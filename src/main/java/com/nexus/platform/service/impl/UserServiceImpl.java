package com.nexus.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nexus.platform.common.api.BusinessException;
import com.nexus.platform.entity.UserEntity;
import com.nexus.platform.mapper.UserMapper;
import com.nexus.platform.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserEntity findById(Integer id) {
        UserEntity user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(100404, "用户不存在");
        }
        return user;
    }

    @Override
    public UserEntity create(String username, String email, String password, String realName, String phone) {
        LambdaQueryWrapper<UserEntity> usernameWrapper = new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, username);
        if (userMapper.selectCount(usernameWrapper) > 0) {
            throw new BusinessException(100409, "用户名已存在");
        }

        LambdaQueryWrapper<UserEntity> emailWrapper = new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getEmail, email);
        if (userMapper.selectCount(emailWrapper) > 0) {
            throw new BusinessException(100409, "邮箱已存在");
        }

        LocalDateTime now = LocalDateTime.now();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRealName(realName);
        user.setPhone(phone);
        user.setStatus((byte) 1);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userMapper.insert(user);
        return user;
    }
}
