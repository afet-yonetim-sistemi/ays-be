package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception to be thrown when a user does not have reserved assignment.
 */
public class AysAssignmentUserNotReservedException extends AysAuthException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -3784057020609338119L;


    /**
     * Constructs a new AysAssignmentUserNotReservedException with the specified userId.
     *
     * @param userId        the userId of user that try to approve assignment.
     */
    public AysAssignmentUserNotReservedException(String userId) {
        super("USER NOT HAVE RESERVED ASSIGNMENT! userId:" + userId);
    }

}
