package com.example.futbet.exception;

import org.springframework.http.HttpStatus;

public class PhaseNotFoundException extends BusinessException {

    public PhaseNotFoundException() {
        super("Phase not found", HttpStatus.NOT_FOUND);
    }
}
