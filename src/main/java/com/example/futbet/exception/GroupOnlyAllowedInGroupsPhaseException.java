package com.example.futbet.exception;

import org.springframework.http.HttpStatus;

public class GroupOnlyAllowedInGroupsPhaseException extends BusinessException {

    public GroupOnlyAllowedInGroupsPhaseException() {
        super("Groups can only be added to a phase of type GROUPS", HttpStatus.CONFLICT);
    }
}
