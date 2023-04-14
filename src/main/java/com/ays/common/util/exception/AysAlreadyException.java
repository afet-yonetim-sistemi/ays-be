package com.ays.common.util.exception;

public abstract class AysAlreadyException extends RuntimeException {

    protected AysAlreadyException(final String message) {
        super(message);
    }

}
