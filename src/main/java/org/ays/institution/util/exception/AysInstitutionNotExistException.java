package org.ays.institution.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;


/**
 * Exception thrown when an institution does not exist in the system.
 */
public class AysInstitutionNotExistException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -8080466932594432592L;

    /**
     * Constructs a new {@code AysInstitutionNotExistException} with the specified ID.
     *
     * @param id the ID of the institution that does not exist
     */
    public AysInstitutionNotExistException(String id) {
        super("INSTITUTION NOT EXIST! ID:" + id);
    }

}
