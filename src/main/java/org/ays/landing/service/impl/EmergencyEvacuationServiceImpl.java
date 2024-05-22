package org.ays.landing.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.landing.model.mapper.EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper;
import org.ays.landing.model.mapper.EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper;
import org.ays.landing.repository.EmergencyEvacuationRepository;
import org.ays.landing.service.EmergencyEvacuationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmergencyEvacuationServiceImpl implements EmergencyEvacuationService {
    private final EmergencyEvacuationRepository emergencyEvacuationRepository;
    private final EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper entityMapper
            = EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper.initialize();
    private final EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper responseMapper
            = EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper.initialize();

    @Override
    public EmergencyEvacuationApplicationResponse addEmergencyEvacuationRequest(EmergencyEvacuationRequest emergencyEvacuationRequest) {
        var emergencyEvacuationEntity = entityMapper.mapForSaving(emergencyEvacuationRequest);

        emergencyEvacuationRepository.save(emergencyEvacuationEntity);
        return responseMapper.map(emergencyEvacuationEntity);
    }
}
