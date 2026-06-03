package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class MatchGenerationException extends BusinessException {

    public MatchGenerationException(String detail) {
        super(detail, HttpStatus.CONFLICT);
    }
}
