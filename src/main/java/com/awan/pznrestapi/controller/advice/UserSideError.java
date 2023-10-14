package com.awan.pznrestapi.controller.advice;

import com.awan.pznrestapi.model.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserSideError {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<WebResponse<String>> httpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex
    ) {

        WebResponse<String> webResponse = WebResponse.<String>builder().errors(ex.getMessage()).build();
        return ResponseEntity.status(ex.getStatusCode()).body(webResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<WebResponse<String>> httpMessageNotReadableException(
            HttpMessageNotReadableException ex
    ) {

        WebResponse<String> webResponse = WebResponse.<String>builder().errors(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(webResponse);

    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<WebResponse<String>> httpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex
    ) {

        WebResponse<String> webResponse = WebResponse.<String>builder().errors(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(webResponse);

    }

}
