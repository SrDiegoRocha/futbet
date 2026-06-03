package com.example.reidopitaco.exception;

import com.example.reidopitaco.enums.TournamentStatus;
import org.springframework.http.HttpStatus;

public class PhaseStructureLockedException extends BusinessException {

    public PhaseStructureLockedException(TournamentStatus status) {
        super(
                "Phase structure is locked while tournament is " + status,
                HttpStatus.CONFLICT
        );
    }
}
