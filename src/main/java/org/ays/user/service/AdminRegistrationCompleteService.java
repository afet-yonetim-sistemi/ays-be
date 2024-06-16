package org.ays.user.service;

import org.ays.user.model.request.AdminRegistrationApplicationCompleteRequest;

/**
 * Admin Register service to perform admin related authentication operations.
 */
public interface AdminRegistrationCompleteService {

    /**
     * Complete an existing admin application.
     *
     * @param registerRequest the AdminUserRegisterRequest entity
     */
    void complete(String applicationId, AdminRegistrationApplicationCompleteRequest registerRequest);

}
