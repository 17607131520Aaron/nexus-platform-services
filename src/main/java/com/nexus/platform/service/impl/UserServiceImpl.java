package com.nexus.platform.service.impl;

import com.nexus.platform.common.api.BusinessException;
import com.nexus.platform.entity.UserEntity;
import com.nexus.platform.repository.UserRepository;
import com.nexus.platform.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(100404, "用户不存在"));
    }

    @Override
    public UserEntity create(String username, String email) {
        userRepository.findByUsername(username).ifPresent(it -> {
            throw new BusinessException(100409, "用户名已存在");
        });

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        return userRepository.save(user);
    }
}
