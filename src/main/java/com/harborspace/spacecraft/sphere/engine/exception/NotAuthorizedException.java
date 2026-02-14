package com.harborspace.spacecraft.sphere.engine.exception;

import org.springframework.http.HttpStatus;

public class NotAuthorizedException extends SphereClientException {
    public NotAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.toString());
    }
}
