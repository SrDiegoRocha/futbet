package com.example.futbet.exception;

import org.springframework.http.HttpStatus;

public class TeamNotInTournamentException extends BusinessException {

    public TeamNotInTournamentException() {
        super("Team is not part of this tournament", HttpStatus.CONFLICT);
    }
}
