package com.ays.auth.util.exception;

import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * An exception that is thrown when a username is not valid.
 * Extends {@link AysAuthException}.
 */
public class UsernameNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8712878086437207740L;

    /**
     * Constructs a new {@code UsernameNotValidException} with a default message.
     */
    public UsernameNotValidException(final String username) {
        super("USERNAME IS NOT VALID! username: " + username);
    }

}
