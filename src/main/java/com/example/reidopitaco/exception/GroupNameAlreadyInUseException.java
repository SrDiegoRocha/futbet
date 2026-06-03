package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class GroupNameAlreadyInUseException extends BusinessException {

    public GroupNameAlreadyInUseException() {
        super("This phase already has a group with that name", HttpStatus.CONFLICT);
    }
}
