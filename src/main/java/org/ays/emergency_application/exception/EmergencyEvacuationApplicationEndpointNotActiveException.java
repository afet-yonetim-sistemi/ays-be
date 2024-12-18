package org.ays.emergency_application.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

public final class EmergencyEvacuationApplicationEndpointNotActiveException extends AysAuthException {

    @Serial
    private static final long serialVersionUID = -5238438967353670872L;

    public EmergencyEvacuationApplicationEndpointNotActiveException() {
        super("emergency evacuation application endpoint is not active!");
    }

}
