package com.ays.admin_user.service;

import com.ays.admin_user.model.AdminUser;
import com.ays.common.model.AysPage;
import com.ays.user.model.dto.request.UserListRequest;
import org.springframework.security.core.Authentication;

/**
 * Admin User service to perform admin user related business operations.
 */
public interface AdminUserService {

    /**
     * Get all Admin Users from database.
     *
     * @param listRequest covering page and pageSize
     * @return Admin User list
     */
    AysPage<AdminUser> getAllAdminUsers(UserListRequest listRequest);
}
