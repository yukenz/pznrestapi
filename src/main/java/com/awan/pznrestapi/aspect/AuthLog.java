package com.awan.pznrestapi.aspect;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.LoginUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthLog {

    @Around(value = "execution(* com.awan.pznrestapi.service.AuthService.authLogin(..)) && args(form))")
    Object userLogin(ProceedingJoinPoint joinPoint, LoginUserRequest form) throws Throwable {

        log.info("username {} try to login", form.getUsername());
        try {
            Object proceed = joinPoint.proceed(joinPoint.getArgs());
            log.info("username {} Success to login", form.getUsername());
            return proceed;
        } catch (Throwable throwable) {
            log.info("username {} Login Failed bcz : {}", form.getUsername(), throwable.getMessage());
            throw throwable;
        }

    }

    @Around(value = "execution(* com.awan.pznrestapi.service.AuthService.authLogout(..)) && args(user))")
    Object userLogout(ProceedingJoinPoint joinPoint, User user) throws Throwable {

        String token = user.getToken();

        log.info("user with token {} want to logout", token);
        try {
            Object proceed = joinPoint.proceed(joinPoint.getArgs());
            log.info("user with token {} Success logout", token);
            return proceed;
        } catch (Throwable throwable) {
            log.info("user with token {} Failed to Logout bcz : {}", token, throwable.getMessage());
            throw throwable;
        }

    }

}
