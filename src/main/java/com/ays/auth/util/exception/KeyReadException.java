package com.ays.auth.util.exception;

import java.io.Serial;

/**
 * Exception class defining fired when a key could net be read.
 */
public class KeyReadException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3273349702435810049L;

    public KeyReadException(Exception exception) {
        super("KEY COULD NOT BE READ!", exception);
    }

}
