package com.ays.admin_user.service;

import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequest;

/**
 * Admin Register service to perform admin user related authentication operations.
 */
public interface AdminUserRegisterService {

    /**
     * Complete an existing admin user application.
     *
     * @param registerRequest the AdminUserRegisterRequest entity
     */
    void completeRegistration(String applicationId,AdminUserRegisterApplicationCompleteRequest registerRequest);

}
