package com.ays.user.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a user is passive and attempting to perform an action that requires a passive user.
 */
public class AysUserAlreadyPassiveException extends AysAlreadyException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -3686691276790127586L;

    /**
     * Constructs a new {@code AysUserAlreadyPassiveException} with the specified id.
     *
     * @param id the id of the passive user
     */
    public AysUserAlreadyPassiveException(String id) {
        super("USER IS ALREADY PASSIVE! id:" + id);
    }

}
