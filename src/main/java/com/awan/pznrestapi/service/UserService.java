package com.awan.pznrestapi.service;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.RegisterUserRequest;
import com.awan.pznrestapi.model.UpdateUserRequest;
import com.awan.pznrestapi.model.UserResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /*
     * Menambahkan user ke database
     * */
    @Transactional
    void register(RegisterUserRequest form);

    @Transactional
    void update(User user, UpdateUserRequest form);

    UserResponse get(User user);
}
