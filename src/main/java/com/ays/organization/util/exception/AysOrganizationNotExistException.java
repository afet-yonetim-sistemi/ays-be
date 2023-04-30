package com.ays.organization.util.exception;

import com.ays.common.util.exception.AysNotExistException;

import java.io.Serial;


/**
 * Exception thrown when an organization does not exist in the system.
 */
public class AysOrganizationNotExistException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -8080466932594432592L;

    /**
     * Constructs a new {@code AysOrganizationNotExistException} with the specified ID.
     *
     * @param id the ID of the organization that does not exist
     */
    public AysOrganizationNotExistException(String id) {
        super("ORGANIZATION NOT EXIST! ID:" + id);
    }

}
