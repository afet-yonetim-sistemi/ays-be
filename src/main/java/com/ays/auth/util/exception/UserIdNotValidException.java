package com.ays.auth.util.exception;

import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * An exception that is thrown when a userId is not valid.
 * Extends {@link AysAuthException}.
 */
public class UserIdNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -1446349672040126110L;

    /**
     * Constructs a new {@code UserIdNotValidException} with a default message.
     */
    public UserIdNotValidException(final String userId) {
        super("USER ID IS NOT VALID! userId: " + userId);
    }

}
