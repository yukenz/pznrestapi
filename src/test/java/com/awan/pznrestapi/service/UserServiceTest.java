package com.awan.pznrestapi.service;

import com.awan.pznrestapi.model.RegisterUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    void testSave() {

        RegisterUserRequest form
                = RegisterUserRequest.builder()
                .username("yukenz").password("awan").name("Yuyun Purniawan")
                .build();
        userService.register(form);

    }
}
