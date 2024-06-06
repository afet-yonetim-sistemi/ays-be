package org.ays.emergency_application.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationRequestToEntityMapper;
import org.ays.emergency_application.repository.EmergencyEvacuationApplicationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;

class EmergencyEvacuationApplicationServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private EmergencyEvacuationApplicationServiceImpl emergencyEvacuationApplicationService;

    @Mock
    private EmergencyEvacuationApplicationRepository emergencyEvacuationApplicationRepository;


    private final EmergencyEvacuationApplicationRequestToEntityMapper emergencyEvacuationApplicationRequestToEntityMapper = EmergencyEvacuationApplicationRequestToEntityMapper.initialize();


    @Test
    void givenValidEmergencyEvacuationRequest_ShouldCreateEmergencyEvacuationApplicationCorrectly() {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .build();

        // When
        EmergencyEvacuationApplicationEntity mockApplicationEntity = emergencyEvacuationApplicationRequestToEntityMapper
                .map(mockApplicationRequest);
        mockApplicationEntity.pending();
        Mockito
                .when(emergencyEvacuationApplicationRepository.save(Mockito.any(EmergencyEvacuationApplicationEntity.class)))
                .thenReturn(mockApplicationEntity);

        // When
        emergencyEvacuationApplicationService.create(mockApplicationRequest);

        // Then
        Mockito.verify(emergencyEvacuationApplicationRepository, times(1))
                .save(Mockito.any(EmergencyEvacuationApplicationEntity.class));
    }

}
