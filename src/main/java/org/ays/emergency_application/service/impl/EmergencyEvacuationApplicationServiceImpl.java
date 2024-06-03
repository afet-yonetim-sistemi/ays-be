package org.ays.emergency_application.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationRequestToEntityMapper;
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


    private final EmergencyEvacuationApplicationRequestToEntityMapper emergencyEvacuationApplicationRequestToEntityMapper = EmergencyEvacuationApplicationRequestToEntityMapper.initialize();

    /**
     * Create an emergency evacuation application to the database
     *
     * @param emergencyEvacuationApplicationRequest The emergency evacuation request containing application information
     */
    @Override
    public void create(EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest) {
        EmergencyEvacuationApplicationEntity applicationEntity = emergencyEvacuationApplicationRequestToEntityMapper
                .map(emergencyEvacuationApplicationRequest);
        applicationEntity.pending();

        emergencyEvacuationApplicationRepository.save(applicationEntity);
    }

}
