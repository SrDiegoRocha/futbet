package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class NotTournamentOwnerException extends BusinessException {

    public NotTournamentOwnerException() {
        super("Only the tournament owner can perform this action", HttpStatus.FORBIDDEN);
    }
}
