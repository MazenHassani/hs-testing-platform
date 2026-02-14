package com.harborspace.spacecraft.sphere.engine.exception;

import org.springframework.http.HttpStatus;

public class ConnectionException extends SphereClientException {
    public ConnectionException(String message) {
        super(message, HttpStatus.NO_CONTENT.toString());
    }
}
