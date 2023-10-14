package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.RegisterUserRequest;
import com.awan.pznrestapi.model.UpdateUserRequest;
import com.awan.pznrestapi.model.UserResponse;
import com.awan.pznrestapi.model.WebResponse;
import com.awan.pznrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Override
    public WebResponse<String> register(RegisterUserRequest userform) {

        userService.register(userform);
        return new WebResponse<>().<String>builder().data("OK").build();
    }

    @Override
    public WebResponse<UserResponse> get(User user) {

        UserResponse userResponse = userService.get(user);

        return WebResponse.<UserResponse>builder()
                .data(userResponse).build();
    }

    @Override
    public WebResponse<String> update(User user, UpdateUserRequest form) {

        userService.update(user, form);

        return WebResponse.<String>builder()
                .data("OK").build();
    }
}
