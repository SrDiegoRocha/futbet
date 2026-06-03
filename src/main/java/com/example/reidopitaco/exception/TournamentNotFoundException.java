package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class TournamentNotFoundException extends BusinessException {

    public TournamentNotFoundException() {
        super("Tournament not found", HttpStatus.NOT_FOUND);
    }
}
