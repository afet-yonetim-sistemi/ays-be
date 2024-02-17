package org.ays.admin_user.service;

import org.ays.admin_user.model.AdminUser;
import org.ays.admin_user.model.dto.request.AdminUserListRequest;
import org.ays.common.model.AysPage;

/**
 * Admin User service to perform admin user related business operations.
 */
public interface AdminUserService {

    /**
     * Get Admin Users by all institutions or by their own institution from database based on admin user type.
     *
     * @param listRequest covering page and pageSize
     * @return Admin User list
     */
    AysPage<AdminUser> getAdminUsers(AdminUserListRequest listRequest);
}
