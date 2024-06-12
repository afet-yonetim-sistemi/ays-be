package org.ays.admin_user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.admin_user.model.AdminUser;
import org.ays.admin_user.model.dto.request.AdminUserListRequest;
import org.ays.admin_user.model.dto.response.AdminUsersResponse;
import org.ays.admin_user.model.mapper.AdminUserToAdminUsersResponseMapper;
import org.ays.admin_user.service.AdminUserService;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin User controller to perform admin api operations.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Deprecated(since = "AdminUserController V2 Production'a alınınca burası silinecektir.", forRemoval = true)
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
    @PostMapping("/admins")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public AysResponse<AysPageResponse<AdminUsersResponse>> getAdminUsers(@RequestBody @Valid AdminUserListRequest listRequest) {
        final AysPage<AdminUser> pageOfAdminUsers = adminUserService.getAdminUsers(listRequest);

        final AysPageResponse<AdminUsersResponse> pageOfAdminUsersResponse = AysPageResponse.<AdminUsersResponse>builder()
                .of(pageOfAdminUsers)
                .content(adminUserToAdminUsersResponseMapper.map(pageOfAdminUsers.getContent()))
                .build();
        return AysResponse.successOf(pageOfAdminUsersResponse);
    }
}
