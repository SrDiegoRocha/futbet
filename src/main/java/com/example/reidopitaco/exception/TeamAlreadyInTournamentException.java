package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class TeamAlreadyInTournamentException extends BusinessException {

    public TeamAlreadyInTournamentException() {
        super("Team is already part of this tournament", HttpStatus.CONFLICT);
    }
}
