package org.ays.emergency_application.service.impl;

import org.ays.emergency_application.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.entity.EmergencyEvacuationEntity;
import org.ays.emergency_application.model.entity.EmergencyEvacuationEntityBuilder;
import org.ays.emergency_application.repository.EmergencyEvacuationApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmergencyEvacuationApplicationServiceImplTest {

    @InjectMocks
    private EmergencyEvacuationApplicationServiceImpl emergencyEvacuationApplicationService;

    @Mock
    private EmergencyEvacuationApplicationRepository emergencyEvacuationApplicationRepository;

    @Test
    void givenValidEmergencyEvacuationRequest_ShouldCreateEmergencyEvacuationApplicationCorrectly() {
        // Given
        var request = new EmergencyEvacuationRequestBuilder().withValidFields().build();
        var evacuationEntity = new EmergencyEvacuationEntityBuilder().withValidFields().build();
        when(emergencyEvacuationApplicationRepository.save(any(EmergencyEvacuationEntity.class))).thenReturn(evacuationEntity);

        // When
        emergencyEvacuationApplicationService.addEmergencyEvacuationRequest(request);

        // Then
        verify(emergencyEvacuationApplicationRepository, times(1)).save(any(EmergencyEvacuationEntity.class));
    }

}
