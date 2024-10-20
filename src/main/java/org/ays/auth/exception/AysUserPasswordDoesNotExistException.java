package org.ays.auth.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when a user password does not exist.
 */
public final class AysUserPasswordDoesNotExistException extends AysAuthException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -9023497278913148659L;

    /**
     * Constructs a new {@link AysUserPasswordDoesNotExistException} with the given password ID.
     *
     * @param passwordId the ID of the password that does not exist
     */
    public AysUserPasswordDoesNotExistException(String passwordId) {
        super("user password does not exist! passwordId:" + passwordId);
    }

}
