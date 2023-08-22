package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when an assignment is deleted and attempting to perform an action that requires an assignment which is already deleted.
 */
public class AysAssignmentAlreadyDeletedException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 7742901037914306311L;

    /**
     * Constructs a new {@code AysAssignmentAlreadyAvailableException} with the specified id.
     *
     * @param id the id of the assignment which is already available
     */
    public AysAssignmentAlreadyDeletedException(String id) {
        super("ASSIGNMENT IS ALREADY DELETED! id:" + id);
    }
}
