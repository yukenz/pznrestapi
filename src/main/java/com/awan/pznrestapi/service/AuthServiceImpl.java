package com.awan.pznrestapi.service;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.LoginUserRequest;
import com.awan.pznrestapi.model.TokenResponse;
import com.awan.pznrestapi.repository.UserRepository;
import com.awan.pznrestapi.utils.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Component
public class AuthServiceImpl implements AuthService {

    private final String wrongUsernameOrPassword = "Username atau Password salah";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ValidatorService validatorService;

    @Override
    public TokenResponse authLogin(LoginUserRequest form) {

        //Will advise when validate fail
        validatorService.validateObject(form);

        //Apakah Username ada ?
        User user = userRepository.findById(form.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, wrongUsernameOrPassword));

        //Apakah password benar?

        if (!BCrypt.checkpw(form.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, wrongUsernameOrPassword);
        }


        //Update token jika sudah tidak valid
        if (
                Objects.isNull(user.getToken()) ||
                        !validatorService.validateTokenDuration(user.getTokenExpired(), Boolean.class)
        ) {
            //Set Token Prop to DB
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpired(
                    System.currentTimeMillis()
                            + Duration.of(10, ChronoUnit.MINUTES).toMillis()
            );
            userRepository.save(user);
        }

        return TokenResponse.builder().token(user.getToken()).expiredAt(user.getTokenExpired()).build();
    }

    @Override
    public void authLogout(User user) {
        user.setToken(null);
        user.setTokenExpired(null);
        userRepository.save(user);
    }


}
