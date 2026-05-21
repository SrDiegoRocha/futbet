package com.example.futbet.exception;

import org.springframework.http.HttpStatus;

public class NoGroupsToDrawException extends BusinessException {

    public NoGroupsToDrawException() {
        super("Phase has no groups configured to draw teams into", HttpStatus.CONFLICT);
    }
}
