package com.ays.auth.util.exception;

import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

public class TokenAlreadyInvalidatedException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 926590327251528609L;

    public TokenAlreadyInvalidatedException(String tokenId) {
        super("TOKEN IS ALREADY INVALIDATED! tokenId: " + tokenId);
    }

}
