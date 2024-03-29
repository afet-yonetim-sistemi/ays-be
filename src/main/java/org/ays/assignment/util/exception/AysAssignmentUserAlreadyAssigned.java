package org.ays.assignment.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception to be thrown when a user already has an assignment.
 */
public class AysAssignmentUserAlreadyAssigned extends AysAlreadyException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -3240549605152804148L;

    /**
     * Constructs a new AysAssignmentUserAlreadyAssigned with the specified userId and assignmentId.
     *
     * @param userId the userId of user that will take assignment.
     */
    public AysAssignmentUserAlreadyAssigned(String userId, String assignmentId) {
        super("USER ALREADY HAS AN UNCOMPLETED ASSIGNMENT! userId:" + userId + " assignmentId:" + assignmentId);
    }

}
