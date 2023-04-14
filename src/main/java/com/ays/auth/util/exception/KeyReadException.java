package com.ays.auth.util.exception;

/**
 * Exception class defining fired when a key could net be read.
 */
public class KeyReadException extends RuntimeException {

    public KeyReadException(Exception exception) {
        super("KEY COULD NOT BE READ!", exception);
    }

}
