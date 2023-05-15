package com.ays.admin_user.controller;

import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.dto.response.AdminUsersResponse;
import com.ays.admin_user.model.mapper.AdminUserToAdminUsersResponseMapper;
import com.ays.admin_user.service.AdminUserService;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.user.model.dto.request.UserListRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
class AdminUserController {

    private final AdminUserService adminUserService;

    private final AdminUserToAdminUsersResponseMapper adminUserToAdminUsersResponseMapper = AdminUserToAdminUsersResponseMapper.initialize();

    /**
     * Gets a list of users in the system.
     * Requires ADMIN authority.
     *
     * @param listRequest The request object containing the list criteria.
     * @return A response object containing a paginated list of users.
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public AysResponse<AysPageResponse<AdminUsersResponse>> getAllAdminUsers(@RequestBody @Valid UserListRequest listRequest) {
        final AysPage<AdminUser> pageOfAdminUsers = adminUserService.getAllAdminUsers(listRequest);

        final AysPageResponse<AdminUsersResponse> pageOfAdminUsersResponse = AysPageResponse.<AdminUsersResponse>builder()
                .of(pageOfAdminUsers)
                .content(adminUserToAdminUsersResponseMapper.map(pageOfAdminUsers.getContent()))
                .build();
        return AysResponse.successOf(pageOfAdminUsersResponse);
    }
}
