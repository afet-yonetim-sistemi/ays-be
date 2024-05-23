package org.ays.landing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.landing.service.EmergencyEvacuationApplicationService;
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
@RequestMapping("/api/v1/emergency-evacuation-application")
@RequiredArgsConstructor
public class EmergencyEvacuationApplicationController {
    private final EmergencyEvacuationApplicationService emergencyEvacuationService;

    /**
     * Creates an emergency evacuation application
     *
     * @param emergencyEvacuationRequest The request object containing emergency evacuation application
     * @return An {@link AysResponse} containing a {@link EmergencyEvacuationApplicationResponse} representing the emergency evacuation response.
     */
    @PostMapping
    public AysResponse<EmergencyEvacuationApplicationResponse> createEmergencyEvacuationApplication(@RequestBody @Valid EmergencyEvacuationRequest emergencyEvacuationRequest) {
        return AysResponse.successOf(emergencyEvacuationService.addEmergencyEvacuationRequest(emergencyEvacuationRequest));
    }

}
