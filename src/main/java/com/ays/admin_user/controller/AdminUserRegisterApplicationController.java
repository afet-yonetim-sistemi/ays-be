package com.ays.admin_user.controller;


import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationCreateResponse;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationSummaryResponse;
import com.ays.admin_user.model.dto.response.AdminUserRegisterApplicationsResponse;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper;
import com.ays.admin_user.service.AdminUserRegisterApplicationService;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin Register Application controller to perform register application api operations for admins.
 */
@Validated
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
class AdminUserRegisterApplicationController {

    private final AdminUserRegisterApplicationService adminUserRegisterApplicationService;


    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper.initialize();


    /**
     * Gets a list of admin user register applications in the system.
     * Requires SUPER_ADMIN authority.
     *
     * @param request The request object containing the list criteria.
     * @return A response object containing a paginated list of admin user register applications.
     */
    @PostMapping("/registration-applications")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public AysResponse<AysPageResponse<AdminUserRegisterApplicationsResponse>> getRegistrationApplications(
            @RequestBody @Valid AdminUserRegisterApplicationListRequest request) {

        final AysPage<AdminUserRegisterApplication> pageOfRegisterApplications = adminUserRegisterApplicationService.getRegistrationApplications(request);
        final AysPageResponse<AdminUserRegisterApplicationsResponse> pageResponseOfRegisterApplication = AysPageResponse
                .<AdminUserRegisterApplicationsResponse>builder()
                .of(pageOfRegisterApplications)
                .content(
                        adminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper
                                .map(pageOfRegisterApplications.getContent())
                )
                .filteredBy(request.getFilter())
                .build();

        return AysResponse.successOf(pageResponseOfRegisterApplication);
    }

    /**
     * Gets an admin user registers application detail in the system.
     * Requires SUPER_ADMIN authority.
     *
     * @param id The id of the register application.
     * @return A response with the register application detail.
     */
    @GetMapping("/registration-application/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public AysResponse<AdminUserRegisterApplicationResponse> getRegistrationApplicationById(
            @PathVariable @UUID String id) {

        final AdminUserRegisterApplication registerApplication = adminUserRegisterApplicationService
                .getRegistrationApplicationById(id);

        return AysResponse.successOf(
                adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.map(registerApplication)
        );
    }

    /**
     * Gets an admin user register application summary in the system.
     * Requires no authority.
     *
     * @param id The id of the register application.
     * @return A response with the register application summary.
     */
    @GetMapping("/registration-application/{id}/summary")
    public AysResponse<AdminUserRegisterApplicationSummaryResponse> getRegistrationApplicationSummaryById(
            @PathVariable @UUID String id) {

        final AdminUserRegisterApplication registerApplication = adminUserRegisterApplicationService
                .getRegistrationApplicationSummaryById(id);

        return AysResponse.successOf(
                adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper.map(registerApplication)
        );
    }

    /**
     * Creates a new admin user register application.
     * Requires SUPER_ADMIN authority.
     *
     * @param createRequest The request object containing the register application details.
     * @return A response object containing the created register application.
     */
    @PostMapping("/registration-application")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public AysResponse<AdminUserRegisterApplicationCreateResponse> createRegistrationApplication(
            @RequestBody @Valid AdminUserRegisterApplicationCreateRequest createRequest) {

        AdminUserRegisterApplication registerApplication = adminUserRegisterApplicationService
                .createRegistrationApplication(createRequest);
        return AysResponse.successOf(
                adminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper
                        .map(registerApplication)
        );
    }

    /**
     * Approves an admin user register application.
     * Requires SUPER_ADMIN authority.
     *
     * @param id The id of the register application.
     * @return A success response.
     */
    @PostMapping("/registration-application/{id}/approve")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public AysResponse<Void> approveRegistrationApplication(@PathVariable @UUID String id) {

        adminUserRegisterApplicationService.approveRegistrationApplication(id);
        return AysResponse.SUCCESS;
    }

}
