package org.ays.landing.service.impl;

import org.ays.landing.controller.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.landing.controller.model.entity.EmergencyEvacuationEntityBuilder;
import org.ays.landing.model.entity.EmergencyEvacuationEntity;
import org.ays.landing.repository.EmergencyEvacuationApplicationRepository;
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
