package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class PhaseGroupNotFoundException extends BusinessException {

    public PhaseGroupNotFoundException() {
        super("Group not found", HttpStatus.NOT_FOUND);
    }
}
