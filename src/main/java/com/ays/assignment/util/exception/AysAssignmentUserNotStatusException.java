package com.ays.assignment.util.exception;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;
import java.util.List;

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
     * @param userId           the userId of user that try to handle assignment.
     * @param assignmentStatuses the assignmentStatuses of assignment that is tried to handle.
     */
    public AysAssignmentUserNotStatusException(String userId, List<AssignmentStatus> assignmentStatuses) {
        super("USER NOT HAVE ASSIGNMENT! userId:" + userId + ", assignmentStatuses:" + assignmentStatuses);
    }

}
