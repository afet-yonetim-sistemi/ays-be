package com.ays.admin_user.service;

import com.ays.admin_user.model.AdminUserRegisterApplication;

/**
 * Admin Users Verification application service, which holds the verification information regarding the system user.
 */
public interface AdminUserRegisterApplicationService {

    /**
     * Get registration application by id.
     *
     * @return Admin User Registration Application
     */
    AdminUserRegisterApplication getRegistrationApplicationById(String id);

}
