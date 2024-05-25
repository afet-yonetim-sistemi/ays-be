package org.ays.emergency_application.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationRequestToEntityMapper;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper;
import org.ays.emergency_application.repository.EmergencyEvacuationApplicationRepository;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
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
class EmergencyEvacuationApplicationServiceImpl implements EmergencyEvacuationApplicationService {
    private final EmergencyEvacuationApplicationRepository emergencyEvacuationApplicationRepository;
    private final EmergencyEvacuationApplicationRequestToEntityMapper entityMapper
            = EmergencyEvacuationApplicationRequestToEntityMapper.initialize();
    private final EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper responseMapper
            = EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper.initialize();

    /**
     * Adds an emergency evacuation application to the database
     *
     * @param emergencyEvacuationApplicationRequest The emergency evacuation request containing application information
     * @return Emergency evacuation application response
     */
    @Override
    public EmergencyEvacuationApplicationResponse create(EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest) {
        var emergencyEvacuationEntity = entityMapper.mapForSaving(emergencyEvacuationApplicationRequest);

        emergencyEvacuationApplicationRepository.save(emergencyEvacuationEntity);
        return responseMapper.map(emergencyEvacuationEntity);
    }
}
