package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a user assignment is assigned and attempting to perform an action that requires an assigned assignment.
 */
public class AysAssignmentAlreadyAssignedException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -4641308511630291631L;

    /**
     * Constructs a new {@code AysAssignmentAlreadyAssignedException} with the specified id.
     *
     * @param id the id of the assignment which is already assigned
     */
    public AysAssignmentAlreadyAssignedException(String id) {
        super("ASSIGNMENT IS ALREADY ASSIGNED! id:" + id);
    }

}
