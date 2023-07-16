package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a user assignment is reserved and attempting to perform an action that requires an assignment which is already reserved.
 */
public class AysUserAssignmentAlreadyReservedException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 5516025383665844979L;

    /**
     * Constructs a new {@code AysAssignmentAlreadyReservedException} with the specified id.
     *
     * @param id the id of the assignment which is already reserved
     */
    public AysUserAssignmentAlreadyReservedException(String id) {
        super("ASSIGNMENT IS ALREADY RESERVED! id:" + id);
    }
}
