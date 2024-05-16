package org.ays.admin_user.service;

import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCompleteRequest;

/**
 * Admin Register service to perform admin related authentication operations.
 */
public interface AdminRegistrationCompleteService {

    /**
     * Complete an existing admin application.
     *
     * @param registerRequest the AdminUserRegisterRequest entity
     */
    void complete(String applicationId, AdminRegisterApplicationCompleteRequest registerRequest);

}
