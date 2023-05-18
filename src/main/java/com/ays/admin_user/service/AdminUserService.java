package com.ays.admin_user.service;

import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.dto.request.AdminUserListRequest;
import com.ays.common.model.AysPage;

/**
 * Admin User service to perform admin user related business operations.
 */
public interface AdminUserService {

    /**
     * Get all Admin Users by all organizations or by their own organization from database based on admin user type.
     *
     * @param listRequest covering page and pageSize
     * @return Admin User list
     */
    AysPage<AdminUser> getAdminUsers(AdminUserListRequest listRequest);
}
