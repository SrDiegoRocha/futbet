package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class AlreadyTournamentMemberException extends BusinessException {

    public AlreadyTournamentMemberException() {
        super("You are already a member of this tournament", HttpStatus.CONFLICT);
    }
}
