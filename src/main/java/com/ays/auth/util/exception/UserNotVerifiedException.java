package com.ays.auth.util.exception;

import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when a user is not verified in the authentication system.
 * This exception should be thrown when a user attempts to authenticate but their account
 * is not yet verified. The exception includes the userId of the user in question.
 */
public class UserNotVerifiedException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 1912268783993029596L;

    /**
     * Creates a new UserNotVerifiedException with the given userId.
     *
     * @param userId the userId of the user who is not verified
     */
    public UserNotVerifiedException(String userId) {
        super("USER IS NOT VERIFIED! userId:" + userId);
    }

}
