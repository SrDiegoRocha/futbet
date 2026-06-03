package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super("Invalid or expired token", HttpStatus.UNAUTHORIZED);
    }
}
