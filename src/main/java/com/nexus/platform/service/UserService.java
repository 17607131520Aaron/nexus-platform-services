package com.nexus.platform.service;

import com.nexus.platform.entity.UserEntity;

public interface UserService {

    UserEntity findById(Long id);

    UserEntity create(String username, String email);
}
