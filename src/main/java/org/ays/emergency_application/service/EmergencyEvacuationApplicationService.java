package org.ays.emergency_application.service;

import org.ays.common.model.AysPage;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;

/**
 * Emergency evacuation application service to perform emergency evacuation related operations
 */
public interface EmergencyEvacuationApplicationService {

    /**
     * Retrieves a page of emergency evacuation applications based on the provided request parameters.
     *
     * @param listRequest The request parameters for retrieving the emergency evacuation applications. This includes pagination and filtering parameters.
     * @return A page of emergency evacuation applications. Each application includes details such as the ID, status, and other related information.
     */
    AysPage<EmergencyEvacuationApplication> getEmergencyEvacuationApplications(EmergencyEvacuationApplicationListRequest listRequest);

    /**
     * Create an emergency evacuation request
     *
     * @param emergencyEvacuationApplicationRequest The emergency evacuation request containing application information
     */
    void create(EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest);

}
