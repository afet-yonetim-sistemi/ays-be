package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a user does not have assignment.
 */
public class AysAssignmentNotExistByUserIdException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -2491381629464952144L;

    /**
     * Constructs a new AysAssignmentNotExistByUserIdException with the specified user ID.
     *
     * @param userId the ID of the user that does not have assignment
     */
    public AysAssignmentNotExistByUserIdException(String userId) {
        super("USER DOES NOT HAVE ASSIGNMENT! userId:" + userId);
    }

}
