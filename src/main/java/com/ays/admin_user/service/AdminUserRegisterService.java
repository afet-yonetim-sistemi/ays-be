package com.ays.admin_user.service;

import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;

/**
 * Admin Register service to perform admin user related authentication operations.
 */
public interface AdminUserRegisterService {

    /**
     * Register to platform.
     *
     * @param registerRequest the AdminUserRegisterRequest entity
     */
    void register(AdminUserRegisterRequest registerRequest);

}
