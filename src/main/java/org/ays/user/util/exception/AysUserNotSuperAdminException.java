package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

// TODO : Add Javadoc
public class AysUserNotSuperAdminException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -2209124211644478126L;

    // TODO : Add Javadoc
    public AysUserNotSuperAdminException(String id) {
        super("USER IS NOT SUPER ADMIN! id:" + id);
    }

}
