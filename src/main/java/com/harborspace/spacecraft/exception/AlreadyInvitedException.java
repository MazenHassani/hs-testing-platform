package com.harborspace.spacecraft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AlreadyInvitedException extends Exception {
    public AlreadyInvitedException(Long courseId, String username) {
        super("You are already enrolled to Course " + courseId);
    }
}
