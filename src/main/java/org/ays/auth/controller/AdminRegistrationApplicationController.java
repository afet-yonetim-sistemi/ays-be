package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToApplicationResponseMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToApplicationsResponseMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToCreateResponseMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToSummaryResponseMapper;
import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationCreateRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationRejectRequest;
import org.ays.auth.model.response.AdminRegistrationApplicationCreateResponse;
import org.ays.auth.model.response.AdminRegistrationApplicationResponse;
import org.ays.auth.model.response.AdminRegistrationApplicationSummaryResponse;
import org.ays.auth.model.response.AdminRegistrationApplicationsResponse;
import org.ays.auth.service.AdminRegistrationApplicationService;
import org.ays.auth.service.AdminRegistrationCompleteService;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
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
 * REST controller for managing admin registration applications.
 *
 * <p>
 * This controller provides endpoints for listing, retrieving, creating, approving,
 * rejecting, and completing admin registration applications.
 * </p>
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class AdminRegistrationApplicationController {

    private final AdminRegistrationApplicationService adminRegistrationApplicationService;
    private final AdminRegistrationCompleteService adminRegistrationCompleteService;

    private final AdminRegistrationApplicationToApplicationsResponseMapper adminRegistrationApplicationToApplicationsResponseMapper = AdminRegistrationApplicationToApplicationsResponseMapper.initialize();
    private final AdminRegistrationApplicationToApplicationResponseMapper adminRegistrationApplicationToApplicationResponseMapper = AdminRegistrationApplicationToApplicationResponseMapper.initialize();
    private final AdminRegistrationApplicationToSummaryResponseMapper adminRegistrationApplicationToSummaryResponseMapper = AdminRegistrationApplicationToSummaryResponseMapper.initialize();
    private final AdminRegistrationApplicationToCreateResponseMapper adminRegistrationApplicationToCreateResponseMapper = AdminRegistrationApplicationToCreateResponseMapper.initialize();

    /**
     * Retrieves all admin registration applications based on the provided request parameters.
     * Requires 'application:registration:list' authority.
     *
     * @param request The request object containing filtering and pagination parameters.
     * @return A response containing a paginated list of admin registration applications.
     */
    @PostMapping("/admin-registration-applications")
    @PreAuthorize("hasAnyAuthority('application:registration:list')")
    public AysResponse<AysPageResponse<AdminRegistrationApplicationsResponse>> findAll(
            @RequestBody @Valid AdminRegistrationApplicationListRequest request) {

        final AysPage<AdminRegistrationApplication> pageOfRegisterApplications = adminRegistrationApplicationService.findAll(request);

        final AysPageResponse<AdminRegistrationApplicationsResponse> pageResponseOfRegisterApplication = AysPageResponse
                .<AdminRegistrationApplicationsResponse>builder()
                .of(pageOfRegisterApplications)
                .content(
                        adminRegistrationApplicationToApplicationsResponseMapper
                                .map(pageOfRegisterApplications.getContent())
                )
                .filteredBy(request.getFilter())
                .build();

        return AysResponse.successOf(pageResponseOfRegisterApplication);
    }

    /**
     * Retrieves a specific admin registration application by its ID.
     * Requires 'application:registration:detail' authority.
     *
     * @param id The ID of the admin registration application to retrieve.
     * @return A response containing the requested admin registration application.
     */
    @GetMapping("/admin-registration-application/{id}")
    @PreAuthorize("hasAnyAuthority('application:registration:detail')")
    public AysResponse<AdminRegistrationApplicationResponse> findById(@PathVariable @UUID String id) {

        final AdminRegistrationApplication registerApplication = adminRegistrationApplicationService.findById(id);

        return AysResponse.successOf(
                adminRegistrationApplicationToApplicationResponseMapper.map(registerApplication)
        );
    }

    /**
     * Retrieves a summary of a specific admin registration application by its ID.
     * Requires no authority.
     *
     * @param id The ID of the admin registration application to retrieve the summary for.
     * @return A response containing the summary of the admin registration application.
     */
    @GetMapping("/admin-registration-application/{id}/summary")
    public AysResponse<AdminRegistrationApplicationSummaryResponse> findSummaryById(@PathVariable @UUID String id) {

        final AdminRegistrationApplication registerApplication = adminRegistrationApplicationService
                .findSummaryById(id);

        return AysResponse.successOf(
                adminRegistrationApplicationToSummaryResponseMapper.map(registerApplication)
        );
    }

    /**
     * Creates a new admin registration application.
     * Requires 'application:registration:create' authority.
     *
     * @param createRequest The request containing details for creating the admin registration application.
     * @return A response containing the created admin registration application details.
     */
    @PostMapping("/admin-registration-application")
    @PreAuthorize("hasAnyAuthority('application:registration:create')")
    public AysResponse<AdminRegistrationApplicationCreateResponse> create(
            @RequestBody @Valid AdminRegistrationApplicationCreateRequest createRequest) {

        AdminRegistrationApplication registrationApplication = adminRegistrationApplicationService
                .create(createRequest);
        return AysResponse.successOf(
                adminRegistrationApplicationToCreateResponseMapper
                        .map(registrationApplication)
        );
    }

    /**
     * Approves an admin registration application.
     * Requires 'application:registration:conclude' authority.
     *
     * @param id The ID of the admin registration application to approve.
     * @return A success response upon successful approval.
     */
    @PostMapping("/admin-registration-application/{id}/approve")
    @PreAuthorize("hasAnyAuthority('application:registration:conclude')")
    public AysResponse<Void> approve(@PathVariable @UUID String id) {

        adminRegistrationApplicationService.approve(id);
        return AysResponse.SUCCESS;
    }

    /**
     * Rejects an admin registration application.
     * Requires 'application:registration:conclude' authority.
     *
     * @param id            The ID of the admin registration application to reject.
     * @param rejectRequest The request containing rejection details.
     * @return A success response upon successful rejection.
     */
    @PostMapping("/admin-registration-application/{id}/reject")
    @PreAuthorize("hasAnyAuthority('application:registration:conclude')")
    public AysResponse<Void> reject(@PathVariable @UUID String id,
                                    @RequestBody @Valid AdminRegistrationApplicationRejectRequest rejectRequest) {

        adminRegistrationApplicationService.reject(id, rejectRequest);
        return AysResponse.SUCCESS;
    }

    /**
     * Marks an admin registration application as complete.
     *
     * @param id              The ID of the admin registration application to mark as complete.
     * @param registerRequest The request containing completion details.
     * @return A success response upon successful completion.
     */
    @PostMapping("/admin-registration-application/{id}/complete")
    public AysResponse<Void> complete(@PathVariable @UUID String id,
                                      @RequestBody @Valid AdminRegistrationApplicationCompleteRequest registerRequest) {

        adminRegistrationCompleteService.complete(id, registerRequest);
        return AysResponse.SUCCESS;
    }

}
