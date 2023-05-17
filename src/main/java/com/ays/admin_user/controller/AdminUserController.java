package com.ays.admin_user.controller;

import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.dto.request.AdminUserListRequest;
import com.ays.admin_user.model.dto.response.AdminUsersResponse;
import com.ays.admin_user.model.mapper.AdminUserToAdminUsersResponseMapper;
import com.ays.admin_user.service.AdminUserService;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin User controller to perform admin api operations.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
class AdminUserController {

    private final AdminUserService adminUserService;

    private final AdminUserToAdminUsersResponseMapper adminUserToAdminUsersResponseMapper = AdminUserToAdminUsersResponseMapper.initialize();

    /**
     * Gets a list of users in the system.
     * Requires ADMIN and SUPER_ADMIN authority.
     *
     * @param listRequest The request object containing the list criteria.
     * @return A response object containing a paginated list of users.
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public AysResponse<AysPageResponse<AdminUsersResponse>> getAllAdminUsers(@RequestBody @Valid AdminUserListRequest listRequest) {
        final AysPage<AdminUser> pageOfAdminUsers = adminUserService.getAdminUsers(listRequest);

        final AysPageResponse<AdminUsersResponse> pageOfAdminUsersResponse = AysPageResponse.<AdminUsersResponse>builder()
                .of(pageOfAdminUsers)
                .content(adminUserToAdminUsersResponseMapper.map(pageOfAdminUsers.getContent()))
                .build();
        return AysResponse.successOf(pageOfAdminUsersResponse);
    }
}
