package org.ays.landing.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.landing.model.mapper.EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper;
import org.ays.landing.model.mapper.EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper;
import org.ays.landing.repository.EmergencyEvacuationApplicationRepository;
import org.ays.landing.service.EmergencyEvacuationApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class implements the interface {@link EmergencyEvacuationApplicationService}
 * It is annotated with {@code @Service} to indicate that it is a service component in the application.
 * The class is also annotated with {@code @RequiredArgsConstructor} to automatically generate a constructor based on the declared final fields.
 * The {@code @Transactional} annotation ensures that all the methods in this class are executed within a transactional context.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class EmergencyEvacuationApplicationServiceImpl implements EmergencyEvacuationApplicationService {
    private final EmergencyEvacuationApplicationRepository emergencyEvacuationApplicationRepository;
    private final EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper entityMapper
            = EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper.initialize();
    private final EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper responseMapper
            = EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper.initialize();

    /**
     * Adds an emergency evacuation application to the database
     *
     * @param emergencyEvacuationRequest The emergency evacuation request containing application information
     * @return Emergency evacuation application response
     */
    @Override
    public EmergencyEvacuationApplicationResponse addEmergencyEvacuationRequest(EmergencyEvacuationRequest emergencyEvacuationRequest) {
        var emergencyEvacuationEntity = entityMapper.mapForSaving(emergencyEvacuationRequest);

        emergencyEvacuationApplicationRepository.save(emergencyEvacuationEntity);
        return responseMapper.map(emergencyEvacuationEntity);
    }
}
