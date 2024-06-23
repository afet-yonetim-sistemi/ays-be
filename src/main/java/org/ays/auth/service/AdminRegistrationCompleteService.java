package org.ays.auth.service;

import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequest;

/**
 * Service interface for completing admin registration applications.
 * Provides functionality to finalize the registration process of an admin based on a given application ID and request.
 */
public interface AdminRegistrationCompleteService {

    /**
     * Completes the admin registration process for a specified application.
     * This method finalizes the registration by processing the provided application ID and details in the complete request.
     *
     * @param applicationId   The unique identifier of the admin registration application to be completed.
     * @param registerRequest The request object containing necessary information to complete the registration.
     *                        This typically includes admin credentials, contact details, and other relevant data.
     */
    void complete(String applicationId, AdminRegistrationApplicationCompleteRequest registerRequest);

}
