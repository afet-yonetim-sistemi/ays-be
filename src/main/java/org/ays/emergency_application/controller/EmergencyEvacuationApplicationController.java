package org.ays.emergency_application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationResponseMapper;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationsResponseMapper;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequest;
import org.ays.emergency_application.model.response.EmergencyEvacuationApplicationResponse;
import org.ays.emergency_application.model.response.EmergencyEvacuationApplicationsResponse;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
import org.ays.emergency_application.util.annotation.CheckEmergencyEvacuationApplicationActivity;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller class for managing emergency evacuation application-related operations via HTTP requests.
 * This controller handles the business operations for emergency evacuation applications in the system.
 */
@Validated
@RestController
@RequiredArgsConstructor
class EmergencyEvacuationApplicationController {

    private final EmergencyEvacuationApplicationService emergencyEvacuationApplicationService;


    private final EmergencyEvacuationApplicationToApplicationsResponseMapper emergencyEvacuationApplicationToApplicationsResponseMapper = EmergencyEvacuationApplicationToApplicationsResponseMapper.initialize();
    private final EmergencyEvacuationApplicationToApplicationResponseMapper emergencyEvacuationApplicationToApplicationResponseMapper = EmergencyEvacuationApplicationToApplicationResponseMapper.initialize();


    /**
     * Handles POST requests for retrieving a paginated list of emergency evacuation applications.
     *
     * @param listRequest The request body containing the parameters for listing emergency evacuation applications.
     * @return A response containing a paginated list of {@link EmergencyEvacuationApplicationsResponse}.
     */
    @PostMapping({"/api/v1/emergency-evacuation-applications", "/api/institution/v1/emergency-evacuation-applications"})
    @PreAuthorize("hasAnyAuthority('application:evacuation:list')")
    public AysResponse<AysPageResponse<EmergencyEvacuationApplicationsResponse>> findAll(
            @RequestBody @Valid EmergencyEvacuationApplicationListRequest listRequest) {

        final AysPage<EmergencyEvacuationApplication> pageOfEmergencyEvacuationApplications = emergencyEvacuationApplicationService.findAll(listRequest);

        final AysPageResponse<EmergencyEvacuationApplicationsResponse> pageOfEmergencyEvacuationApplicationsResponse = AysPageResponse.<EmergencyEvacuationApplicationsResponse>builder()
                .of(pageOfEmergencyEvacuationApplications)
                .content(emergencyEvacuationApplicationToApplicationsResponseMapper.map(pageOfEmergencyEvacuationApplications.getContent()))
                .build();
        return AysResponse.successOf(pageOfEmergencyEvacuationApplicationsResponse);
    }


    /**
     * Handles GET requests for retrieving the details of an emergency evacuation application by its ID.
     *
     * @param id the ID of the emergency evacuation application to retrieve
     * @return a response entity containing the details of the emergency evacuation application
     */
    @GetMapping({"/api/v1/emergency-evacuation-application/{id}", "/api/institution/v1/emergency-evacuation-application/{id}"})
    @PreAuthorize("hasAuthority('application:evacuation:detail')")
    public AysResponse<EmergencyEvacuationApplicationResponse> findById(@PathVariable @UUID String id) {
        final EmergencyEvacuationApplication emergencyEvacuationApplication = emergencyEvacuationApplicationService.findById(id);
        final EmergencyEvacuationApplicationResponse emergencyEvacuationApplicationResponse = emergencyEvacuationApplicationToApplicationResponseMapper.map(emergencyEvacuationApplication);
        return AysResponse.successOf(emergencyEvacuationApplicationResponse);
    }


    /**
     * Endpoint to create a new emergency evacuation application.
     * This method accepts a POST request with the emergency evacuation application details in the request body.
     * The request body is validated before processing.
     *
     * @param emergencyEvacuationApplicationRequest the details of the emergency evacuation application
     * @return a response indicating the success of the operation
     */
    @CheckEmergencyEvacuationApplicationActivity
    @PostMapping({"/api/v1/emergency-evacuation-application", "/api/landing/v1/emergency-evacuation-application"})
    public AysResponse<Void> create(@RequestBody @Valid EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest) {
        emergencyEvacuationApplicationService.create(emergencyEvacuationApplicationRequest);
        return AysResponse.success();
    }


    /**
     * Updates an existing Emergency Evacuation Application.
     * This method accepts a PUT request
     * <p>
     * This endpoint updates the Emergency Evacuation Application with the specified ID using the provided update request.
     * The request body and path variable validated before processing.
     * The user must have the authority 'application:evacuation:update' to access this endpoint.
     * </p>
     *
     * @param id            the unique identifier of the Emergency Evacuation Application to be updated
     * @param updateRequest the request object containing the details to update the Emergency Evacuation Application
     * @return a response indicating the success of the update operation
     */
    @PutMapping({"/api/v1/emergency-evacuation-application/{id}", "/api/institution/v1/emergency-evacuation-application/{id}"})
    @PreAuthorize("hasAuthority('application:evacuation:update')")
    public AysResponse<Void> update(@PathVariable @UUID final String id,
                                    @RequestBody @Valid final EmergencyEvacuationApplicationUpdateRequest updateRequest) {

        emergencyEvacuationApplicationService.update(id, updateRequest);
        return AysResponse.success();
    }

}
