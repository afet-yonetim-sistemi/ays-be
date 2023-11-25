package com.ays.admin_user.controller;


import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper;
import com.ays.admin_user.service.AdminUserRegisterApplicationService;
import com.ays.common.model.dto.response.AysResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin Register Application controller to perform register application api operations for admins.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminUserRegisterApplicationController {

    private final AdminUserRegisterApplicationService adminUserRegisterApplicationService;


    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.initialize();

    /**
     * Gets an admin user register application detail in the system.
     * Requires SUPER_ADMIN authority.
     *
     * @param id The id of the register application.
     * @return A response with the register application detail.
     */
    @GetMapping("/registration-application/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public AysResponse<AdminUserRegisterApplicationResponse> getRegistrationApplicationById(@PathVariable String id) {
        final AdminUserRegisterApplication registerApplication = adminUserRegisterApplicationService
                .getRegistrationApplicationById(id);

        return AysResponse.successOf(
                adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.map(registerApplication)
        );
    }

}
