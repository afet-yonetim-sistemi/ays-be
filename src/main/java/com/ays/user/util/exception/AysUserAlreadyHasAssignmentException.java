package com.ays.user.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a user has assignment and attempting to update user support status.
 */
public class AysUserAlreadyHasAssignmentException extends AysAlreadyException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -3686691276790127586L;

    /**
     * Constructs a new {@code AysUserAlreadyHasAssignmentException} with the specified id and assignmentId.
     *
     * @param id the id of the user
     * @param assignmentId the id of the assignment
     */
    public AysUserAlreadyHasAssignmentException(String id, String assignmentId) {
        super("USER ALREADY HAS ASSIGNMENT! id:" + id + " assignmentId:" + assignmentId);
    }

}
