package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a user assignment is in progress and attempting to perform an action that requires an assignment which is already in progress.
 */
public class AysUserAssignmentAlreadyInProgressException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -3095992510410240081L;

    /**
     * Constructs a new {@code AysAssignmentAlreadyInProgressException} with the specified id.
     *
     * @param id the id of the assignment which is already in progress
     */
    public AysUserAssignmentAlreadyInProgressException(String id) {
        super("ASSIGNMENT IS ALREADY IN PROGRESS! id:" + id);
    }

}

