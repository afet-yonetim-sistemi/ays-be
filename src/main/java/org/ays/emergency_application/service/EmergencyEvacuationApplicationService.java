package org.ays.emergency_application.service;

import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;

/**
 * Emergency evacuation application service to perform emergency evacuation related operations
 */
public interface EmergencyEvacuationApplicationService {

    /**
     * Create an emergency evacuation request
     *
     * @param emergencyEvacuationApplicationRequest The emergency evacuation request containing application information
     */
    void create(EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest);

}
