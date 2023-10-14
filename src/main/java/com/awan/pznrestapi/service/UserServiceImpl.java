package com.awan.pznrestapi.service;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.RegisterUserRequest;
import com.awan.pznrestapi.model.UpdateUserRequest;
import com.awan.pznrestapi.model.UserResponse;
import com.awan.pznrestapi.repository.UserRepository;
import com.awan.pznrestapi.utils.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidatorService validatorService;

    @Override
    public void register(RegisterUserRequest form) {

        //Will advise when validate fail
        validatorService.validateObject(form);

        if (userRepository.existsById(form.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username sudah ada");
        }

        User user = User.builder()
                .username(form.getUsername())
                .password(BCrypt.hashpw(form.getPassword(), BCrypt.gensalt()))
                .name(form.getName())
                .build();

        userRepository.save(user);

    }

    @Override
    public void update(User user, UpdateUserRequest form) {

        validatorService.validateObject(form);

        String name = form.getName();
        String password = form.getPassword();

        if (Objects.nonNull(name)) {
            user.setName(name);
        }

        if (Objects.nonNull(password)) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }

        userRepository.save(user);

    }

    @Override
    public UserResponse get(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .tokenExpiredAt(user.getTokenExpired())
                .build();
    }
}
