package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class TournamentFullException extends BusinessException {

    public TournamentFullException(String resource) {
        super("Tournament is full: max " + resource + " reached", HttpStatus.CONFLICT);
    }
}
