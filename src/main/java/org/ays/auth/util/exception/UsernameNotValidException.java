package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * An exception that is thrown when a username is not valid.
 * Extends {@link AysAuthException}.
 */
@Deprecated(since = "UsernameNotValidException V2 Production'a alınınca burası silinecektir.", forRemoval = true)
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
