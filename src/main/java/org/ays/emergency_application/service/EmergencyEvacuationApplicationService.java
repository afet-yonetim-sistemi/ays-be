package org.ays.emergency_application.service;

import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.dto.response.EmergencyEvacuationApplicationResponse;

/**
 * Emergency evacuation application service to perform emergency evacuation related operations
 */
public interface EmergencyEvacuationApplicationService {

    /**
     * Adds an emergency evacuation request
     *
     * @param emergencyEvacuationApplicationRequest The emergency evacuation request containing application information
     * @return Emergency evacuation application response containing resulting data from the business logic
     */
    EmergencyEvacuationApplicationResponse addEmergencyEvacuationRequest(EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest);
}
