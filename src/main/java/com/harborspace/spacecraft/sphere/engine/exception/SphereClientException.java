package com.harborspace.spacecraft.sphere.engine.exception;

import lombok.Getter;

public class SphereClientException extends RuntimeException {
    @Getter
    private String code;

    public SphereClientException(String message, String code) {
        super(message);
        this.code = code;
    }
}
