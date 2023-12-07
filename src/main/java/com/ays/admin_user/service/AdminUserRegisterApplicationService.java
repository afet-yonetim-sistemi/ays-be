package com.ays.admin_user.service;

import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.common.model.AysPage;


/**
 * Admin Users Verification application service, which holds the verification information regarding the system user.
 */
public interface AdminUserRegisterApplicationService {

    /**
     * Get registration applications based on the specified filters in the {@link AdminUserRegisterApplicationListRequest}
     *
     * @param listRequest covering page and pageSize
     * @return Admin User Registration Application list
     */
    AysPage<AdminUserRegisterApplication> getRegistrationApplications(AdminUserRegisterApplicationListRequest listRequest);


    /**
     * Get registration application by id.
     *
     * @return Admin User Registration Application
     */
    AdminUserRegisterApplication getRegistrationApplicationById(String id);

    /**
     * Get registration application summary by id.
     *
     * @param id registration application id
     * @return Admin User Registration Application
     */
    AdminUserRegisterApplication getRegistrationApplicationSummaryById(String id);
}
