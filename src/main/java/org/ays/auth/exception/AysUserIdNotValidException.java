package org.ays.auth.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * An exception that is thrown when a userId is not valid.
 * Extends {@link AysAuthException}.
 */
public final class AysUserIdNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -1446349672040126110L;

    /**
     * Constructs a new {@link AysUserIdNotValidException} with a default message.
     */
    public AysUserIdNotValidException(final String userId) {
        super("user id is not valid! userId: " + userId);
    }

}
