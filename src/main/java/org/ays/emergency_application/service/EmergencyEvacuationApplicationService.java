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

    /**
     * Updates an existing Emergency Evacuation Application with the provided details.
     * <p>
     * This method updates the Emergency Evacuation Application identified by the given ID
     * with the information contained in the update request. If the application does not exist
     * or cannot be accessed by the current user, an exception is thrown.
     * </p>
     *
     * @param id            the unique identifier of the Emergency Evacuation Application to be updated
     * @param updateRequest the request object containing the details to update the Emergency Evacuation Application
     */
    void update(String id, EmergencyEvacuationApplicationUpdateRequest updateRequest);

}
