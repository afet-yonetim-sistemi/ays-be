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
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller class for managing emergency evacuation application-related operations via HTTP requests.
 * This controller handles the business operations for emergency evacuation applications in the system.
 * The mapping path for this controller is "/api/v1/emergency-evacuation-application".
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
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
    @PostMapping("/emergency-evacuation-applications")
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
    @GetMapping("/emergency-evacuation-application/{id}")
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
    @PostMapping("/emergency-evacuation-application")
    public AysResponse<Void> create(@RequestBody @Valid EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest) {
        emergencyEvacuationApplicationService.create(emergencyEvacuationApplicationRequest);
        return AysResponse.SUCCESS;
    }

    // TODO : Javadoc
    // TODO : Write tests
    @PutMapping("emergency-evacuation-application/{id}")
    @PreAuthorize("hasAuthority('application:evacuation:update')")
    public AysResponse<Void> update(
            @PathVariable @UUID final String id,
            @RequestBody @Valid final EmergencyEvacuationApplicationUpdateRequest updateRequest
    ) {
        emergencyEvacuationApplicationService.update(id, updateRequest);

        return AysResponse.SUCCESS;
    }


}
