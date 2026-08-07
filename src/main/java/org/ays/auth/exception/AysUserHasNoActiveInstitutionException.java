package org.ays.auth.exception;

import org.ays.common.exception.AysConflictException;

import java.io.Serial;

/**
 * Exception thrown when a user has no active institution assigned.
 */
public final class AysUserHasNoActiveInstitutionException extends AysConflictException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -1696445586369875802L;

    /**
     * Constructs a new {@link AysUserHasNoActiveInstitutionException} with the specified userId.
     *
     * @param userId the id of the user who has no active institution assigned.
     */
    public AysUserHasNoActiveInstitutionException(final String userId) {
        super(String.format("User with id %s has no active institution assigned!", userId));
    }

}
