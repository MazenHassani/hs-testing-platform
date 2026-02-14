package com.harborspace.spacecraft.sphere.engine.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends SphereClientException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.toString());
    }
}
