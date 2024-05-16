package org.ays.admin_user.service;

import org.ays.admin_user.model.AdminRegistrationApplication;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCreateRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationRejectRequest;
import org.ays.admin_user.model.dto.request.AdminRegistrationApplicationListRequest;
import org.ays.common.model.AysPage;


/**
 * Admin Users Verification application service, which holds the verification information regarding the system user.
 */
public interface AdminRegisterApplicationService {

    /**
     * Get registration applications based on the specified filters in the {@link AdminRegistrationApplicationListRequest}
     *
     * @param listRequest covering page and pageSize
     * @return Admin User Registration Application list
     */
    AysPage<AdminRegistrationApplication> findAll(AdminRegistrationApplicationListRequest listRequest);


    /**
     * Get registration application by id.
     *
     * @return Admin User Registration Application
     */
    AdminRegistrationApplication findById(String id);

    /**
     * Get registration application summary by id.
     *
     * @param id registration application id
     * @return Admin User Registration Application
     */
    AdminRegistrationApplication findAllSummaryById(String id);

    /**
     * Create a new admin register application.
     *
     * @param request The request object containing the register application details.
     * @return A response object containing the created register application.
     */
    AdminRegistrationApplication create(AdminRegisterApplicationCreateRequest request);

    /**
     * Approve a new admin register application.
     *
     * @param id The id of the register application.
     */
    void approve(String id);

    /**
     * Rejects an admin register application.
     * Requires SUPER_ADMIN authority.
     *
     * @param id The id of the register application.
     */
    void reject(String id, AdminRegisterApplicationRejectRequest request);

}
