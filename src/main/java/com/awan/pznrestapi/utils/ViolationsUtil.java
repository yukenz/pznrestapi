package com.awan.pznrestapi.utils;

import jakarta.validation.ConstraintViolation;

import java.util.Set;
import java.util.stream.Collectors;

public class ViolationsUtil {
    public static String violationsToString(Set<ConstraintViolation<?>> violations) {

        String violationsMessage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));

        return violationsMessage;
    }
}
