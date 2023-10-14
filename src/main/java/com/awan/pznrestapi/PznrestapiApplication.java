package com.awan.pznrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class PznrestapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PznrestapiApplication.class, args);
    }

}
