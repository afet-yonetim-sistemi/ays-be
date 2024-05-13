package org.ays.admin_user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.admin_user.model.AdminUserRegisterApplication;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequest;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationCreateResponse;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationSummaryResponse;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationsResponse;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper;
import org.ays.admin_user.service.AdminUserRegisterApplicationService;
import org.ays.admin_user.service.AdminUserRegisterService;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
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
    private final AdminUserRegisterService adminUserRegisterService;

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
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'application:registration:list')")
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
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'application:registration:detail')")
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
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'application:registration:conclude')")
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
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'application:registration:conclude')")
    public AysResponse<Void> approveRegistrationApplication(@PathVariable @UUID String id) {

        adminUserRegisterApplicationService.approveRegistrationApplication(id);
        return AysResponse.SUCCESS;
    }

    /**
     * Rejects an admin user register application.
     * Requires SUPER_ADMIN authority.
     *
     * @param id The id of the register application.
     * @return A response object containing the rejected register application.
     */
    @PostMapping("/registration-application/{id}/reject")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'application:registration:conclude')")
    public AysResponse<Void> rejectRegistrationApplication(@PathVariable @UUID String id,
                                                           @RequestBody @Valid AdminUserRegisterApplicationRejectRequest request) {

        adminUserRegisterApplicationService.rejectRegistrationApplication(id, request);
        return AysResponse.SUCCESS;
    }

    /**
     * This endpoint allows admins to complete their admin-user register applications.
     *
     * @param registerRequest An {@link AdminUserRegisterApplicationCompleteRequest} object required to complete the application.
     * @return An {@link AysResponse} containing a Void object with success message.
     */
    @PostMapping("/registration-application/{id}/complete")
    public AysResponse<Void> completeRegistration(@PathVariable @UUID String id,
                                                  @RequestBody @Valid AdminUserRegisterApplicationCompleteRequest registerRequest) {
        adminUserRegisterService.completeRegistration(id, registerRequest);
        return AysResponse.SUCCESS;
    }

}
