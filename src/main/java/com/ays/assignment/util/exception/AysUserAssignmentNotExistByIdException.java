package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a user assignment with a given ID does not exist.
 */
public class AysUserAssignmentNotExistByIdException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -1656529656159558925L;

    /**
     * Constructs a new AysAssignmentNotExistByIdException with the specified assignment ID.
     *
     * @param id the ID of the assignment that does not exist
     */
    public AysUserAssignmentNotExistByIdException(String id) {
        super("ASSIGNMENT NOT EXIST! id:" + id);
    }

}
