package com.harborspace.spacecraft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TestHasnotStartedException extends Exception {
    public TestHasnotStartedException(Long testId) {
        super("The test with Id: " + testId.toString() + " hasn't started yet!");
    }
}
