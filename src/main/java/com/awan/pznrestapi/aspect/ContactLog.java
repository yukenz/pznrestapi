package com.awan.pznrestapi.aspect;

import com.awan.pznrestapi.entity.User;
import com.awan.pznrestapi.model.SearchContactRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ContactLog {

    @Around(value = "execution(* com.awan.pznrestapi.service.ContactService.search(..)) && args(user,form))")
    Object contactSearch(ProceedingJoinPoint joinPoint, User user, SearchContactRequest form) throws Throwable {

        String username = user.getUsername();

        log.info("user {} want to search", username);
        try {
            Object proceed = joinPoint.proceed(joinPoint.getArgs());
            log.info("search with {}", form);
            return proceed;
        } catch (Throwable throwable) {
            log.info("user {} Failed to Search bcz : {}", username, throwable.getMessage());
            throw throwable;
        }

    }

}
