package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends BusinessException {

    public TeamNotFoundException() {
        super("Team not found", HttpStatus.NOT_FOUND);
    }
}
