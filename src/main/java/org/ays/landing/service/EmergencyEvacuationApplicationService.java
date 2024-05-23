package org.ays.landing.service;

import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;

/**
 * Emergency evacuation application service to perform emergency evacuation related operations
 */
public interface EmergencyEvacuationApplicationService {

    /**
     * Adds an emergency evacuation request
     *
     * @param emergencyEvacuationRequest The emergency evacuation request containing application information
     * @return Emergency evacuation application response containing resulting data from the business logic
     */
    EmergencyEvacuationApplicationResponse addEmergencyEvacuationRequest(EmergencyEvacuationRequest emergencyEvacuationRequest);
}
