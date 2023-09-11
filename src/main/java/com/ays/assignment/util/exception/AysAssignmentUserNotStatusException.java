package com.ays.assignment.util.exception;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception to be thrown when a user does not have reserved assignment.
 */
public class AysAssignmentUserNotStatusException extends AysAuthException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -3784057020609338119L;


    /**
     * Constructs a new AysAssignmentUserNotReservedException with the specified userId.
     *
     * @param assignmentStatus the assignmentStatus of assignment that is tried to handle.
     * @param userId           the userId of user that try to handle assignment.
     */
    public AysAssignmentUserNotStatusException(AssignmentStatus assignmentStatus, String userId) {
        super("USER NOT HAVE " + assignmentStatus + " ASSIGNMENT! userId:" + userId);
    }

}
