package com.example.futbet.exception;

import com.example.futbet.enums.TournamentStatus;
import org.springframework.http.HttpStatus;

public class PhaseStructureLockedException extends BusinessException {

    public PhaseStructureLockedException(TournamentStatus status) {
        super(
                "Phase structure is locked while tournament is " + status,
                HttpStatus.CONFLICT
        );
    }
}
