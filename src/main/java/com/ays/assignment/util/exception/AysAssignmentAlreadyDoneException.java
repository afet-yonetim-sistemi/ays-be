package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when an assignment is done and attempting to perform an action that requires an assignment which is already done.
 */
public class AysAssignmentAlreadyDoneException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 4457246038851186682L;

    /**
     * Constructs a new {@code AysAssignmentAlreadyDoneException} with the specified id.
     *
     * @param id the id of the assignment which is already done
     */
    public AysAssignmentAlreadyDoneException(String id) {
        super("ASSIGNMENT IS ALREADY DONE! id:" + id);
    }

}
