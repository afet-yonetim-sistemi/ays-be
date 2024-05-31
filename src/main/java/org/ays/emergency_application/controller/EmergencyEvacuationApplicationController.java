package org.ays.emergency_application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.dto.response.EmergencyEvacuationApplicationsResponse;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationsResponseMapper;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller class for managing emergency evacuation application-related operations via HTTP requests.
 * This controller handles the business operations for emergency evacuation applications in the system.
 * The mapping path for this controller is "/api/v1/emergency-evacuation-application".
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class EmergencyEvacuationApplicationController {

    private final EmergencyEvacuationApplicationService emergencyEvacuationApplicationService;


    private final EmergencyEvacuationApplicationToApplicationsResponseMapper emergencyEvacuationApplicationToApplicationsResponseMapper = EmergencyEvacuationApplicationToApplicationsResponseMapper.initialize();

    // TODO AYS-222 : Add Javadoc
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

}
