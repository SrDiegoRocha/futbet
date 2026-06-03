package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class PhaseFinalizeException extends BusinessException {

    public PhaseFinalizeException(String detail) {
        super(detail, HttpStatus.CONFLICT);
    }
}
