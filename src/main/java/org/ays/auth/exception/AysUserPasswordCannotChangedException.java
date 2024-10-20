package org.ays.auth.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when a user password cannot be changed.
 */
public final class AysUserPasswordCannotChangedException extends AysAuthException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -2214328005741759939L;

    /**
     * Constructs a new {@link AysUserPasswordCannotChangedException} with the given password ID.
     *
     * @param passwordId the ID of the password that cannot be changed
     */
    public AysUserPasswordCannotChangedException(String passwordId) {
        super("user password cannot be changed! passwordId:" + passwordId);
    }

}
