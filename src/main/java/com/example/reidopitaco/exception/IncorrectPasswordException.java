package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class IncorrectPasswordException extends BusinessException {

    public IncorrectPasswordException() {
        super("Current password is incorrect", HttpStatus.BAD_REQUEST);
    }
}
