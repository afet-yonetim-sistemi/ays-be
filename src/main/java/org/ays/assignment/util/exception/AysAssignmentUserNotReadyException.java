package org.ays.assignment.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception to be thrown when a user is not ready to take assignment.
 */
public class AysAssignmentUserNotReadyException extends AysAlreadyException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -5396757921363054579L;

    /**
     * Constructs a new AysAssignmentUserNotReadyException with the specified userId.
     *
     * @param userId        the userId of user that will take assignment.
     * @param institutionId the institutionId of user that will take assignment.
     */
    public AysAssignmentUserNotReadyException(String userId, String institutionId) {
        super("USER NOT READY TO TAKE ASSIGNMENT! userId:" + userId + ", institutionId:" + institutionId);
    }

}
