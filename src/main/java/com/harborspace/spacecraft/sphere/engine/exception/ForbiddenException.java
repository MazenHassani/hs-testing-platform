package com.harborspace.spacecraft.sphere.engine.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends SphereClientException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN.toString());
    }
}
