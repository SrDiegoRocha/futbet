package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class ZoneNotFoundException extends BusinessException {

    public ZoneNotFoundException() {
        super("Zone not found", HttpStatus.NOT_FOUND);
    }
}
