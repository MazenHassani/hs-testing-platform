package com.harborspace.spacecraft.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AlreadyStartedException extends Exception {
    public AlreadyStartedException(String resourceName) {
        super("The " + resourceName + " has already started!");
    }
}
