package com.example.futbet.exception;

import org.springframework.http.HttpStatus;

public class IncorrectPasswordException extends BusinessException {

    public IncorrectPasswordException() {
        super("Current password is incorrect", HttpStatus.BAD_REQUEST);
    }
}
