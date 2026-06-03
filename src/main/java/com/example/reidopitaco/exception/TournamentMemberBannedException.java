package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class TournamentMemberBannedException extends BusinessException {

    public TournamentMemberBannedException() {
        super("You are banned from this tournament", HttpStatus.FORBIDDEN);
    }
}
