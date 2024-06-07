package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception to be thrown when a user is not a super admin but tries to perform an action that requires super admin privileges.
 */
public final class AysUserNotSuperAdminException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -2209124211644478126L;

    /**
     * Constructs a new AysUserNotSuperAdminException with the specified user ID.
     *
     * @param id the ID of the user who is not a super admin
     */
    public AysUserNotSuperAdminException(String id) {
        super("user is not super admin! id:" + id);
    }

}
