package org.ays.emergency_application.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.EmergencyEvacuationApplicationBuilder;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.ays.emergency_application.model.filter.EmergencyEvacuationApplicationFilter;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationRequestToDomainMapper;
import org.ays.emergency_application.model.request.*;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationReadPort;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationSavePort;
import org.ays.emergency_application.util.exception.EmergencyEvacuationApplicationNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class EmergencyEvacuationApplicationServiceImplTest extends AysUnitTest {

    @InjectMocks
    private EmergencyEvacuationApplicationServiceImpl emergencyEvacuationApplicationService;

    @Mock
    private EmergencyEvacuationApplicationReadPort emergencyEvacuationApplicationReadPort;

    @Mock
    private EmergencyEvacuationApplicationSavePort emergencyEvacuationApplicationSavePort;


    private final EmergencyEvacuationApplicationRequestToDomainMapper emergencyEvacuationApplicationRequestToDomainMapper = EmergencyEvacuationApplicationRequestToDomainMapper.initialize();

    @Mock
    private AysIdentity aysIdentity;

    @Test
    @SuppressWarnings("unchecked")
    void givenEmergencyEvacuationApplicationListRequest_whenFilterNotGiven_thenReturnApplicationsPage() {

        // Given
        EmergencyEvacuationApplicationListRequest mockApplicationListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysPageable aysPageable = mockApplicationListRequest.getPageable();
        EmergencyEvacuationApplicationFilter filter = mockApplicationListRequest.getFilter();

        List<EmergencyEvacuationApplication> mockApplications = List.of(
                new EmergencyEvacuationApplicationBuilder()
                        .withValidValues()
                        .withoutApplicant()
                        .build()
        );
        AysPage<EmergencyEvacuationApplication> mockApplicationsPage = AysPageBuilder
                .from(mockApplications, mockApplicationListRequest.getPageable(), mockApplicationListRequest.getFilter());

        Mockito.when(emergencyEvacuationApplicationReadPort.findAll(aysPageable, filter))
                .thenReturn(mockApplicationsPage);

        // Then
        AysPage<EmergencyEvacuationApplication> applicationPage = emergencyEvacuationApplicationService
                .findAll(mockApplicationListRequest);

        AysPageBuilder.assertEquals(mockApplicationsPage, applicationPage);

        // Verify
        Mockito.verify(emergencyEvacuationApplicationReadPort, Mockito.times(1))
                .findAll(aysPageable, filter);
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenEmergencyEvacuationApplicationListRequest_whenApplicationStatusIsAvailable_thenReturnApplicationsPage() {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withStatuses(Set.of(EmergencyEvacuationApplicationStatus.PENDING))
                .build();

        // When
        AysPageable aysPageable = mockListRequest.getPageable();
        EmergencyEvacuationApplicationFilter filter = mockListRequest.getFilter();

        List<EmergencyEvacuationApplication> mockApplications = List.of(
                new EmergencyEvacuationApplicationBuilder()
                        .withValidValues()
                        .withoutApplicant()
                        .build()
        );

        AysPage<EmergencyEvacuationApplication> mockApplicationPage = AysPageBuilder
                .from(mockApplications, mockListRequest.getPageable(), mockListRequest.getFilter());

        Mockito.when(emergencyEvacuationApplicationReadPort.findAll(aysPageable, filter))
                .thenReturn(mockApplicationPage);

        // Then
        AysPage<EmergencyEvacuationApplication> applicationPage = emergencyEvacuationApplicationService
                .findAll(mockListRequest);

        AysPageBuilder.assertEquals(mockApplicationPage, applicationPage);

        // Verify
        Mockito.verify(emergencyEvacuationApplicationReadPort, Mockito.times(1))
                .findAll(aysPageable, filter);
    }


    @Test
    void givenEmergencyEvacuationApplicationId_whenGettingEmergencyEvacuationApplication_thenReturnEmergencyEvacuationApplication() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        EmergencyEvacuationApplication mockApplication = new EmergencyEvacuationApplicationBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(emergencyEvacuationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        // Then
        EmergencyEvacuationApplication emergencyEvacuationApplication = emergencyEvacuationApplicationService.findById(mockId);

        // Verify
        Mockito.verify(emergencyEvacuationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Assertions.assertNotNull(emergencyEvacuationApplication);
        Assertions.assertEquals(mockId, emergencyEvacuationApplication.getId());
    }

    @Test
    void givenEmergencyEvacuationApplicationId_whenEmergencyEvacuationApplicationNotFound_thenThrowAysEmergencyEvacuationApplicationNotExistException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(emergencyEvacuationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                EmergencyEvacuationApplicationNotExistException.class,
                () -> emergencyEvacuationApplicationService.findById(mockId)
        );

        // Verify
        Mockito.verify(emergencyEvacuationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenValidEmergencyEvacuationRequest_whenRequestValid_thenCreateEmergencyEvacuationApplication() {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .build();

        // When
        EmergencyEvacuationApplication mockApplication = emergencyEvacuationApplicationRequestToDomainMapper
                .map(mockApplicationRequest);

        mockApplication.pending();

        Mockito.when(emergencyEvacuationApplicationSavePort.save(Mockito.any(EmergencyEvacuationApplication.class)))
                .thenReturn(mockApplication);

        // When
        emergencyEvacuationApplicationService.create(mockApplicationRequest);

        // Then
        Mockito.verify(emergencyEvacuationApplicationSavePort, Mockito.times(1))
                .save(Mockito.any(EmergencyEvacuationApplication.class));
    }

    @Test
    void givenUpdateRequest_whenApplicationExists_thenUpdateApplication() {
        // Given
        String applicationId = "dbb3287a-563d-4d85-a978-bcd699294daa";
        EmergencyEvacuationApplication existingApplication = new EmergencyEvacuationApplicationBuilder()
                .withValidValues()
                .withoutApplicant()
                .build();

        EmergencyEvacuationApplicationUpdateRequest updateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withSeatingCount(10)
                .withHasObstaclePersonExist(false)
                .withStatus(EmergencyEvacuationApplicationStatus.IN_PROGRESS)
                .withNotes("Updated notes")
                .build();

        Mockito.when(emergencyEvacuationApplicationReadPort.findById(applicationId))
                .thenReturn(Optional.of(existingApplication));
        Mockito.when(aysIdentity.getInstitutionId()).thenReturn("mockInstitutionId");

        // When
        emergencyEvacuationApplicationService.update(applicationId, updateRequest);

        // Then
        Mockito.verify(emergencyEvacuationApplicationReadPort, Mockito.times(1))
                .findById(applicationId);
        Mockito.verify(emergencyEvacuationApplicationSavePort, Mockito.times(1))
                .save(existingApplication);

        Assertions.assertEquals("mockInstitutionId", existingApplication.getInstitution().getId());
        Assertions.assertEquals(updateRequest.getSeatingCount(), existingApplication.getSeatingCount());
        Assertions.assertEquals(updateRequest.getHasObstaclePersonExist(), existingApplication.getHasObstaclePersonExist());
        Assertions.assertEquals(updateRequest.getStatus(), existingApplication.getStatus());
        Assertions.assertEquals(updateRequest.getNotes(), existingApplication.getNotes());
    }

    @Test
    void givenUpdateRequest_whenApplicationDoesNotExist_thenThrowException(){
        // Given
        String applicationId = AysRandomUtil.generateUUID();
        EmergencyEvacuationApplicationUpdateRequest updateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .build();

        // Mock repository behavior
        Mockito.when(emergencyEvacuationApplicationReadPort.findById(applicationId))
                .thenReturn(Optional.empty());

        // When/Then
        Assertions.assertThrows(
                EmergencyEvacuationApplicationNotExistException.class,
                () -> emergencyEvacuationApplicationService.update(applicationId, updateRequest)
        );

        // Verify
        Mockito.verify(emergencyEvacuationApplicationReadPort, Mockito.times(1))
                .findById(applicationId);

        Mockito.verify(emergencyEvacuationApplicationSavePort, Mockito.never())
                .save(Mockito.any(EmergencyEvacuationApplication.class));
    }
}
