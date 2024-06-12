package org.ays.emergency_application.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;


/**
 * Exception thrown when an emergency evacuation does not exist in the system.
 */
public class AysEmergencyEvacuationApplicationNotExistException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -8440969651937835029L;

    /**
     * Constructs a new {@link AysEmergencyEvacuationApplicationNotExistException} with the specified ID.
     *
     * @param id the ID of the emergency evacuation that does not exist.
     */
    public AysEmergencyEvacuationApplicationNotExistException(String id) {
        super("emergency evacuation application does not exist! id:" + id);
    }
}
