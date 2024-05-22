package org.ays.landing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.landing.service.EmergencyEvacuationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emergency-evacuation")
@RequiredArgsConstructor
public class EmergencyEvacuationController {
    private final EmergencyEvacuationService emergencyEvacuationService;

    @PostMapping
    public AysResponse<EmergencyEvacuationApplicationResponse> createEmergencyEvacuationApplication(@RequestBody @Valid EmergencyEvacuationRequest emergencyEvacuationRequest) {
        return AysResponse.successOf(emergencyEvacuationService.addEmergencyEvacuationRequest(emergencyEvacuationRequest));
    }

}
