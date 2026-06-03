package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class NotTournamentMemberException extends BusinessException {

    public NotTournamentMemberException() {
        super("You are not an active member of this tournament", HttpStatus.FORBIDDEN);
    }
}
