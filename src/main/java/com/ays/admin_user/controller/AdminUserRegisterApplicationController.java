package com.ays.admin_user.controller;


import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationsResponse;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper;
import com.ays.admin_user.service.AdminUserRegisterApplicationService;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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


    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper.initialize();

    /**
     * Gets a list of admin user register applications in the system.
     * Requires SUPER_ADMIN authority.
     *
     * @param request The request object containing the list criteria.
     * @return A response object containing a paginated list of admin user register applications.
     */
    @PostMapping("/registration-applications")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public AysResponse<AysPageResponse<AdminUserRegisterApplicationsResponse>> getRegistrationApplications(@RequestBody AdminUserRegisterApplicationListRequest request) {
        final AysPage<AdminUserRegisterApplication> pageOfRegisterApplications = adminUserRegisterApplicationService.getRegistrationApplications(request);
        final AysPageResponse<AdminUserRegisterApplicationsResponse> pageResponseOfRegisterApplication = AysPageResponse
                .<AdminUserRegisterApplicationsResponse>builder()
                .of(pageOfRegisterApplications)
                .content(adminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper.map(pageOfRegisterApplications.getContent()))
                .filteredBy(request.getFilter())
                .build();

        return AysResponse.successOf(pageResponseOfRegisterApplication);
    }

}
