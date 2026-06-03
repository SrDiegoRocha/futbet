package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class InvalidZoneException extends BusinessException {

    public InvalidZoneException(String detail) {
        super(detail, HttpStatus.CONFLICT);
    }
}
