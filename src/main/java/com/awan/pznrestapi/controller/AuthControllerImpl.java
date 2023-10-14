package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.LoginUserRequest;
import com.awan.pznrestapi.model.TokenResponse;
import com.awan.pznrestapi.model.WebResponse;
import com.awan.pznrestapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthControllerImpl implements AuthController {

    @Autowired
    private AuthService authService;

    @Override
    public WebResponse<TokenResponse> authLogin(LoginUserRequest form) {

        TokenResponse tokenResponse = authService.authLogin(form);

        return WebResponse.<TokenResponse>builder()
                .data(tokenResponse)
                .build();
    }

    @Override
    public WebResponse<String> authLogout(User user) {
        authService.authLogout(user);
        return WebResponse.<String>builder()
                .data("Sukses LogOut")
                .build();
    }


}
