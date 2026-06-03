package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class PhaseNotFoundException extends BusinessException {

    public PhaseNotFoundException() {
        super("Phase not found", HttpStatus.NOT_FOUND);
    }
}
