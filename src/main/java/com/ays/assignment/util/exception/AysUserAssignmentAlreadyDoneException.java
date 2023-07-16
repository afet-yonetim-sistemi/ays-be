package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a user assignment is done and attempting to perform an action that requires an assignment which is already done.
 */
public class AysUserAssignmentAlreadyDoneException extends AysAlreadyException {

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
    public AysUserAssignmentAlreadyDoneException(String id) {
        super("ASSIGNMENT IS ALREADY DONE! id:" + id);
    }

}
