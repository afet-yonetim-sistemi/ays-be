package org.ays.auth.util.exception;

import org.ays.auth.model.enums.AysSourcePage;
import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when a user attempts to access a page or resource they are not authorized to view.
 * <p>
 * This exception is used to signify that the user identified by {@code userId} is not permitted
 * to access the specified {@link AysSourcePage}.
 * </p>
 * <p>
 * It extends {@link AysAuthException}, which provides the base exception for all authentication-related issues in the AYS application.
 * </p>
 *
 * @see AysAuthException
 */
public final class AysUserDoesNotAccessPageException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -1433927234948182106L;

    /**
     * Constructs a new {@code UserDoesNotAccessPageException} with a detailed message
     * indicating the user ID and the source page that the user attempted to access.
     *
     * @param userId     the ID of the user who is attempting unauthorized access
     * @param sourcePage the page or resource the user attempted to access
     */
    public AysUserDoesNotAccessPageException(String userId, AysSourcePage sourcePage) {
        super("user not allowed to access the source page! userId:" + userId + " sourcePage:" + sourcePage);
    }

}
