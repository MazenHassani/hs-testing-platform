package com.harborspace.spacecraft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends Exception {
    public InvalidRequestException(String entity) {
        super("Unable to process you request because " + entity + " is invalid!");
    }
}
