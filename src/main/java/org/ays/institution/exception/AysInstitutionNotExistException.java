package org.ays.institution.exception;

import org.ays.common.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception thrown when an institution does not exist in the system.
 */
public final class AysInstitutionNotExistException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -8080466932594432592L;

    /**
     * Constructs a new {@link AysInstitutionNotExistException} with the specified ID.
     *
     * @param id the ID of the institution that does not exist
     */
    public AysInstitutionNotExistException(String id) {
        super("institution does not exist! ID:" + id);
    }

}
