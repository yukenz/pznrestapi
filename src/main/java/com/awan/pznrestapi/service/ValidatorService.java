package com.awan.pznrestapi.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class ValidatorService {

    @Autowired
    private Validator validator;

    public void validateObject(Object object) {

        Set<ConstraintViolation<Object>>
                violations = validator.validate(object);

        if (violations.size() != 0) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void validateTokenDuration(Long tokenExpiredAt) {

        if (System.currentTimeMillis() > tokenExpiredAt) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Durasi Token Tidak Valid");
        }

    }

    public <T extends Boolean> T validateTokenDuration(Long tokenExpiredAt, Class<T> bool) {

        if (System.currentTimeMillis() > tokenExpiredAt) {
            return (T) T.FALSE;
        }

        return (T) T.TRUE;

    }

}
