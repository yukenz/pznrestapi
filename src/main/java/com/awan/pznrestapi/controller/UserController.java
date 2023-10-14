package com.awan.pznrestapi.controller;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.RegisterUserRequest;
import com.awan.pznrestapi.model.UpdateUserRequest;
import com.awan.pznrestapi.model.UserResponse;
import com.awan.pznrestapi.model.WebResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public interface UserController {

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<String> register(@RequestBody RegisterUserRequest form);

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<UserResponse> get(User user);

    @PatchMapping(
            path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<String> update(
            User user,
            @RequestBody UpdateUserRequest form
    );
}
