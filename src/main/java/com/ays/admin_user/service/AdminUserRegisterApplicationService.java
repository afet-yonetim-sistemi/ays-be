package com.ays.admin_user.service;

import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.common.model.AysPage;

/**
 * Admin users registration application service, which holds the registration information regarding the system user.
 */
public interface AdminUserRegisterApplicationService {

    /**
     * Get registration applications based on the specified filters in the {@link AdminUserRegisterApplicationListRequest}
     *
     * @param listRequest covering page and pageSize
     * @return Admin User Registration Application list
     */
    AysPage<AdminUserRegisterApplication> getRegistrationApplications(AdminUserRegisterApplicationListRequest listRequest);

}
