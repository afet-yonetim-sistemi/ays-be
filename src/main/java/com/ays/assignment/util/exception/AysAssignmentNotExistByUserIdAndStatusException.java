package com.ays.assignment.util.exception;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception to be thrown when a user does not have assignment by status.
 */
public class AysAssignmentNotExistByUserIdAndStatusException extends AysAuthException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -3784057020609338119L;


    /**
     * Constructs a new AysAssignmentNotExistByUserIdAndStatusException with the specified userId.
     *
     * @param userId           the userId of user that try to handle assignment.
     * @param assignmentStatus the assignmentStatus of assignment that is tried to handle.
     */
    public AysAssignmentNotExistByUserIdAndStatusException(String userId, AssignmentStatus assignmentStatus) {
        super("ASSIGNMENT NOT EXIST! userId:" + userId + ", assignmentStatus:" + assignmentStatus);
    }

}
