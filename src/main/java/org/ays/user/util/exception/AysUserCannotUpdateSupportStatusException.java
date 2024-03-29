package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a user has assignment and attempting to update user support status.
 */
public class AysUserCannotUpdateSupportStatusException extends AysAlreadyException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -4727384445754973709L;

    /**
     * Constructs a new {@code AysUserCannotUpdateSupportStatusException} with the specified id and assignmentId.
     *
     * @param id the id of the user
     */
    public AysUserCannotUpdateSupportStatusException(String id, String assignmentId) {
        super("USER CANNOT UPDATE SUPPORT STATUS BECAUSE USER HAS ASSIGNMENT! id:" + id + " assignmentId:" + assignmentId);
    }

}
