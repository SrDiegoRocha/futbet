package com.example.reidopitaco.exception;

import com.example.reidopitaco.enums.TournamentStatus;
import org.springframework.http.HttpStatus;

public class TournamentNotEditableException extends BusinessException {

    public TournamentNotEditableException(TournamentStatus status, String detail) {
        super("Cannot modify tournament in status " + status + ": " + detail, HttpStatus.CONFLICT);
    }
}
