package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class PredictionNotFoundException extends BusinessException {

    public PredictionNotFoundException() {
        super("Prediction not found", HttpStatus.NOT_FOUND);
    }
}
