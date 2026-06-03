package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class MatchNotFoundException extends BusinessException {

    public MatchNotFoundException() {
        super("Match not found", HttpStatus.NOT_FOUND);
    }
}
