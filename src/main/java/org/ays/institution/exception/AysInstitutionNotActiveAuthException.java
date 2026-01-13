package org.ays.institution.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when attempting to authenticate a institution that is not active.
 */
public final class AysInstitutionNotActiveAuthException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -8742356091824567329L;

    /**
     * Constructs a new {@link AysInstitutionNotActiveAuthException}with the specified institutionId.
     *
     * @param institutionId the id of the institution that is not active.
     */
    public AysInstitutionNotActiveAuthException(String institutionId) {
        super("institution is not active! institutionId:" + institutionId);
    }

}
