package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class TeamNotOwnedException extends BusinessException {

    public TeamNotOwnedException() {
        super("You can only add your own teams to a tournament", HttpStatus.FORBIDDEN);
    }
}
