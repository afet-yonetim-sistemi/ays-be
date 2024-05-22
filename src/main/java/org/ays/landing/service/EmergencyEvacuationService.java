package org.ays.landing.service;

import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;

public interface EmergencyEvacuationService {
    EmergencyEvacuationApplicationResponse addEmergencyEvacuationRequest(EmergencyEvacuationRequest emergencyEvacuationRequest);
}
