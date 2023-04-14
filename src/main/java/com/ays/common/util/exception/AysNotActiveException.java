package com.ays.common.util.exception;

public abstract class AysNotActiveException extends RuntimeException {

    protected AysNotActiveException(final String message) {
        super(message);
    }

}
