package com.example.reidopitaco.exception;

import com.example.reidopitaco.enums.TournamentStatus;
import org.springframework.http.HttpStatus;

public class InvalidStatusTransitionException extends BusinessException {

    public InvalidStatusTransitionException(TournamentStatus from, TournamentStatus to) {
        super("Cannot transition tournament from " + from + " to " + to, HttpStatus.CONFLICT);
    }
}
