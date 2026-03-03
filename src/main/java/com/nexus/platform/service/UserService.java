package com.nexus.platform.service;

import com.nexus.platform.entity.UserEntity;

public interface UserService {

    UserEntity findById(Integer id);

    UserEntity create(String username, String email, String password, String realName, String phone);
}
