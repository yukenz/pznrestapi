package com.awan.pznrestapi.controller.advice;

import com.awan.pznrestapi.controller.UserController;
import com.awan.pznrestapi.model.WebResponse;
import com.awan.pznrestapi.utils.ViolationsUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(
            ConstraintViolationException ex
    ) {

        WebResponse<String> webResponse = WebResponse.<String>builder()
                .errors(ViolationsUtil.violationsToString(ex.getConstraintViolations()))
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(webResponse);

    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> responseStatusException(
            ResponseStatusException ex
    ) {

        WebResponse<String> webResponse = WebResponse.<String>builder()
                .errors(ex.getMessage()).build();

        return ResponseEntity.status(ex.getStatusCode()).body(webResponse);
    }

}
