package com.example.futbet.exception;

import org.springframework.http.HttpStatus;

/**
 * Lançada quando uma operação é pedida para uma fase cujo {@code phaseType} não a suporta
 * (ex.: standings em fase KNOCKOUT, ou bracket em fase ROUND_ROBIN/GROUPS).
 */
public class InvalidPhaseTypeException extends BusinessException {

    public InvalidPhaseTypeException(String detail) {
        super(detail, HttpStatus.CONFLICT);
    }
}
