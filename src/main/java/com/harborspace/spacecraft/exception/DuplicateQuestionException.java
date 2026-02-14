package com.harborspace.spacecraft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateQuestionException extends Exception {
    public DuplicateQuestionException() {
        super("Duplicate questions added to the test!");
    }
}