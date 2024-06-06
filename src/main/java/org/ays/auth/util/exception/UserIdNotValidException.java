package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * An exception that is thrown when a userId is not valid.
 * Extends {@link AysAuthException}.
 */
public final class UserIdNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -1446349672040126110L;

    /**
     * Constructs a new {@code UserIdNotValidException} with a default message.
     */
    public UserIdNotValidException(final String userId) {
        super("user id is not valid! userId: " + userId);
    }

}
