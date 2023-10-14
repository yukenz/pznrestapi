package com.awan.pznrestapi.aspect;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.LoginUserRequest;
import com.awan.pznrestapi.model.UpdateUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class UserLog {

    @Before(value = "execution(* com.awan.pznrestapi.service.UserService.get(..)) && args(user)")
    void beforeGetCurrent(JoinPoint joinPoint, User user) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm.SSS");
        Date nowTime = new Date(System.currentTimeMillis());
        Date tokenExpiredTime = new Date(user.getTokenExpired());

        log.info(
                "{} melakukan GET, memiliki token expired pada {}, dan waktu sekarang {}"
                , user.getUsername()
                , sdf.format(tokenExpiredTime)
                , sdf.format(nowTime)
        );

    }

    @Around(value = "execution(* com.awan.pznrestapi.service.UserService.update(..)) && args(user,form)")
    Object aroundUpdate(ProceedingJoinPoint joinPoint, User user, UpdateUserRequest form) throws Throwable {

        String username = user.getUsername();

        log.info("username {} try to Update Data", username);
        log.info(form.toString());
        try {
            Object proceed = joinPoint.proceed(joinPoint.getArgs());
            log.info("username {} Success Update Data", username);
            return proceed;
        } catch (Throwable throwable) {
            log.info("username {} Failed Update Data bcz : {}", username, throwable.getMessage());
            throw throwable;
        }

    }
}
