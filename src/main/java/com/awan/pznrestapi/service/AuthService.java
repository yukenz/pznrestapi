package com.awan.pznrestapi.service;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.LoginUserRequest;
import com.awan.pznrestapi.model.TokenResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    @Transactional
    TokenResponse authLogin(LoginUserRequest form);

    @Transactional
    void authLogout(User user);
}
