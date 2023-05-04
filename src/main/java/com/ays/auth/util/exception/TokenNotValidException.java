package com.ays.auth.util.exception;

import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

public class TokenNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -5404410121820902017L;

    public TokenNotValidException(String jwt, Throwable cause) {
        super("TOKEN IS NOT VALID! token: " + jwt, cause);
    }

}
