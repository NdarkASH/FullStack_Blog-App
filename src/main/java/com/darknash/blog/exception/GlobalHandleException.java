package com.darknash.blog.exception;

import com.darknash.blog.dto.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppResponse> EntityHandleException(EntityNotFoundException e) {
        AppResponse appResponse = AppResponse.builder()
                .msg(e.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return new ResponseEntity<>(appResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HandleException.class)
    public ResponseEntity<AppResponse> handleException(HandleException e) {
        AppResponse appResponse = AppResponse.builder()
                .msg(e.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return new ResponseEntity<>(appResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppResponse> badCredentialsException(BadCredentialsException e) {
        AppResponse appResponse = AppResponse.builder()
                .msg(e.getMessage())
                .code(HttpStatus.UNAUTHORIZED.value())
                .build();
        return new ResponseEntity<>(appResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<AppResponse> illegalAccessException(IllegalAccessException e) {
        AppResponse appResponse = AppResponse.builder()
                .msg(e.getMessage())
                .code(HttpStatus.FORBIDDEN.value())
                .build();
        return new ResponseEntity<>(appResponse, HttpStatus.FORBIDDEN);
    }
}
