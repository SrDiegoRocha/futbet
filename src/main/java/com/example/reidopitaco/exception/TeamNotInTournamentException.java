package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class TeamNotInTournamentException extends BusinessException {

    public TeamNotInTournamentException() {
        super("Team is not part of this tournament", HttpStatus.CONFLICT);
    }
}
