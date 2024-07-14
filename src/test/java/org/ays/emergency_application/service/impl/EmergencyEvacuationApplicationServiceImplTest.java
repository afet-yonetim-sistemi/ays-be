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
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequestBuilder;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequestBuilder;
import org.ays.emergency_application.model.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationReadPort;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationSavePort;
import org.ays.emergency_application.util.exception.EmergencyEvacuationApplicationNotExistException;
import org.ays.institution.model.InstitutionBuilder;
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

    @Mock
    private AysIdentity identity;


    private final EmergencyEvacuationApplicationRequestToDomainMapper emergencyEvacuationApplicationRequestToDomainMapper = EmergencyEvacuationApplicationRequestToDomainMapper.initialize();


    @Test
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

        // Then
        emergencyEvacuationApplicationService.create(mockApplicationRequest);

        // Verify
        Mockito.verify(emergencyEvacuationApplicationSavePort, Mockito.times(1))
                .save(Mockito.any(EmergencyEvacuationApplication.class));
    }


    @Test
    void givenValidIdAndUpdateRequest_whenApplicationExists_thenUpdateApplication() {
        // Given
        String mockId = "dbb3287a-563d-4d85-a978-bcd699294daa";

        EmergencyEvacuationApplicationUpdateRequest mockUpdateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        EmergencyEvacuationApplication mockApplication = new EmergencyEvacuationApplicationBuilder()
                .withValidValues()
                .withId(mockId)
                .withoutApplicant()
                .withoutInstitution()
                .withSeatingCount(5)
                .withoutHasObstaclePersonExist()
                .withStatus(EmergencyEvacuationApplicationStatus.PENDING)
                .withoutNotes()
                .build();
        Mockito.when(emergencyEvacuationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        String mockInstitutionId = AysRandomUtil.generateUUID();
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);

        EmergencyEvacuationApplication mockUpdatedApplication = new EmergencyEvacuationApplicationBuilder()
                .withValidValues()
                .withId(mockId)
                .withoutApplicant()
                .withInstitution(new InstitutionBuilder().withValidValues().withId(mockInstitutionId).build())
                .withSeatingCount(mockUpdateRequest.getSeatingCount())
                .withHasObstaclePersonExist(mockUpdateRequest.getHasObstaclePersonExist())
                .withStatus(mockUpdateRequest.getStatus())
                .withNotes(mockUpdateRequest.getNotes())
                .build();
        Mockito.when(emergencyEvacuationApplicationSavePort.save(Mockito.any(EmergencyEvacuationApplication.class)))
                .thenReturn(mockUpdatedApplication);

        // Then
        emergencyEvacuationApplicationService.update(mockId, mockUpdateRequest);

        Assertions.assertEquals(mockInstitutionId, mockUpdatedApplication.getInstitution().getId());
        Assertions.assertEquals(mockUpdateRequest.getSeatingCount(), mockUpdatedApplication.getSeatingCount());
        Assertions.assertEquals(mockUpdateRequest.getHasObstaclePersonExist(), mockUpdatedApplication.getHasObstaclePersonExist());
        Assertions.assertEquals(mockUpdateRequest.getStatus(), mockUpdatedApplication.getStatus());
        Assertions.assertEquals(mockUpdateRequest.getNotes(), mockUpdatedApplication.getNotes());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationReadPort, Mockito.times(1))
                .findById(mockId);

        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

        Mockito.verify(emergencyEvacuationApplicationSavePort, Mockito.times(1))
                .save(Mockito.any(EmergencyEvacuationApplication.class));
    }

    @Test
    void givenValidIdAndUpdateRequest_whenApplicationDoesNotExist_thenThrowEmergencyEvacuationApplicationNotExistException() {
        // Given
        String mockId = "3019dc69-cf68-4618-8612-3124dc069ff0";

        EmergencyEvacuationApplicationUpdateRequest mockUpdateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(emergencyEvacuationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                EmergencyEvacuationApplicationNotExistException.class,
                () -> emergencyEvacuationApplicationService.update(mockId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(emergencyEvacuationApplicationReadPort, Mockito.times(1))
                .findById(mockId);

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();

        Mockito.verify(emergencyEvacuationApplicationSavePort, Mockito.never())
                .save(Mockito.any(EmergencyEvacuationApplication.class));
    }

}
