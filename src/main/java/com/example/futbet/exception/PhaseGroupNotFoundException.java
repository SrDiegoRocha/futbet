package com.example.futbet.exception;

import org.springframework.http.HttpStatus;

public class PhaseGroupNotFoundException extends BusinessException {

    public PhaseGroupNotFoundException() {
        super("Group not found", HttpStatus.NOT_FOUND);
    }
}
