package org.ays.emergency_application.service;

import org.ays.common.model.AysPage;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequest;

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
    AysPage<EmergencyEvacuationApplication> findAll(EmergencyEvacuationApplicationListRequest listRequest);

    /**
     * Create an emergency evacuation request
     *
     * @param emergencyEvacuationApplicationRequest The emergency evacuation request containing application information
     */
    void create(EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest);

    /**
     * Retrieves the details of a specific emergency evacuation application by its ID.
     *
     * @param id The ID of the emergency evacuation application.
     * @return The emergency evacuation application with the specified ID, or null if not found.
     */
    EmergencyEvacuationApplication findById(String id);

    // TODO : Add javadoc
    void update(
            String id,
            EmergencyEvacuationApplicationUpdateRequest updateRequest
    );
}
