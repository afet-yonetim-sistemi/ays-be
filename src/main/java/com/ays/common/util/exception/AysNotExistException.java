package com.ays.common.util.exception;

public abstract class AysNotExistException extends RuntimeException {

    protected AysNotExistException(final String message) {
        super(message);
    }

}
