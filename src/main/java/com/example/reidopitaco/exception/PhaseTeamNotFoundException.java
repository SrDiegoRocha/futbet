package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

public class PhaseTeamNotFoundException extends BusinessException {

    public PhaseTeamNotFoundException() {
        super("Team is not in this phase", HttpStatus.NOT_FOUND);
    }
}
