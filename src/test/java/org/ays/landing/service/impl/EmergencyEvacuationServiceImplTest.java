package org.ays.landing.service.impl;

import org.ays.landing.controller.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.landing.controller.model.dto.response.EmergencyEvacuationResponseBuilder;
import org.ays.landing.controller.model.entity.EmergencyEvacuationEntityBuilder;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.landing.model.entity.EmergencyEvacuationEntity;
import org.ays.landing.model.mapper.EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper;
import org.ays.landing.model.mapper.EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper;
import org.ays.landing.repository.EmergencyEvacuationRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmergencyEvacuationServiceImplTest {

    @InjectMocks
    private EmergencyEvacuationServiceImpl emergencyEvacuationService;

    @Mock
    private EmergencyEvacuationRepository emergencyEvacuationRepository;

    @Test
    void givenValidEmergencyEvacuationRequest_ShouldCreateEmergencyEvacuationApplicationCorrectly() {
        // Given
        var request = new EmergencyEvacuationRequestBuilder().withValidFields().build();
        var evacuationEntity = new EmergencyEvacuationEntityBuilder().withValidFields().build();
        when(emergencyEvacuationRepository.save(any(EmergencyEvacuationEntity.class))).thenReturn(evacuationEntity);

        // When
        emergencyEvacuationService.addEmergencyEvacuationRequest(request);

        // Then
        verify(emergencyEvacuationRepository, times(1)).save(any(EmergencyEvacuationEntity.class));
    }

}
