package com.awan.pznrestapi.repository;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void setUserTokenMinus60m() {

        User user = userRepository.findById("awan")
                .orElseThrow(() -> new TestAbortedException("User not found"));

        long nowMillisMinus60m = System.currentTimeMillis() + Duration.of(60, ChronoUnit.MINUTES).toMillis();

        user.setTokenExpired(nowMillisMinus60m);

        userRepository.save(user);

    }
}
