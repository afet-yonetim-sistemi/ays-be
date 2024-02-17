package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * TokenAlreadyInvalidatedException is an exception class that is thrown when attempting to invalidate a token
 * that is already marked as invalid.
 * It extends the AysAuthException class and provides a specific error message indicating the already invalidated token.
 */
public class TokenAlreadyInvalidatedException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 926590327251528609L;

    /**
     * Constructs a new TokenAlreadyInvalidatedException with the given token ID.
     *
     * @param tokenId the ID of the token that is already invalidated
     */
    public TokenAlreadyInvalidatedException(String tokenId) {
        super("TOKEN IS ALREADY INVALIDATED! tokenId: " + tokenId);
    }

}
