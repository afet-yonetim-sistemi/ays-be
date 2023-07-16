package com.ays.location.util.exception;

import com.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a location with a given ID does not exist.
 */
public class AysUserLocationNotExistByIdException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 2448425046125508633L;

    /**
     * Constructs a new AysLocationNotExistByIdException with the specified assignment ID.
     *
     * @param id the ID of the location that does not exist
     */
    public AysUserLocationNotExistByIdException(String id) {
        super("USER LOCATION NOT EXIST! id:" + id);
    }

}
