package org.ays.emergency_application.port.impl;

import org.ays.AysUnitTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPageableBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.EmergencyEvacuationApplicationBuilder;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntityBuilder;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.ays.emergency_application.model.filter.EmergencyEvacuationApplicationFilter;
import org.ays.emergency_application.model.filter.EmergencyEvacuationApplicationFilterBuilder;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationEntityToDomainMapper;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToEntityMapper;
import org.ays.emergency_application.repository.EmergencyEvacuationApplicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.times;

class EmergencyEvacuationApplicationAdapterTest extends AysUnitTest {

    @InjectMocks
    private EmergencyEvacuationApplicationAdapter emergencyEvacuationApplicationAdapter;

    @Mock
    private EmergencyEvacuationApplicationRepository emergencyEvacuationApplicationRepository;


    private final EmergencyEvacuationApplicationEntityToDomainMapper emergencyEvacuationApplicationEntityToDomainMapper = EmergencyEvacuationApplicationEntityToDomainMapper.initialize();
    private final EmergencyEvacuationApplicationToEntityMapper emergencyEvacuationApplicationToEntityMapper = EmergencyEvacuationApplicationToEntityMapper.initialize();


    @Test
    @SuppressWarnings("unchecked")
    void givenValidAysPageableWithoutFilter_whenApplicationsFound_thenReturnApplicationsPage() {

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withValidValues()
                .withoutOrders()
                .build();
        EmergencyEvacuationApplicationFilter mockFilter = null;

        // When
        List<EmergencyEvacuationApplicationEntity> mockEntities = List.of(
                new EmergencyEvacuationApplicationEntityBuilder()
                        .withValidValues()
                        .withoutApplicant()
                        .build()
        );
        Page<EmergencyEvacuationApplicationEntity> mockApplicationEntitiesPage = new PageImpl<>(mockEntities);
        Mockito.when(emergencyEvacuationApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockApplicationEntitiesPage);

        List<EmergencyEvacuationApplication> mockApplications = emergencyEvacuationApplicationEntityToDomainMapper
                .map(mockEntities);
        AysPage<EmergencyEvacuationApplication> mockApplicationsPage = AysPageBuilder
                .from(mockApplications, mockAysPageable, mockFilter);

        // Then
        AysPage<EmergencyEvacuationApplication> applicationsPage = emergencyEvacuationApplicationAdapter
                .findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockApplicationsPage, applicationsPage);

        // Verify
        Mockito.verify(emergencyEvacuationApplicationRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenValidAysPageableAndFilter_whenApplicationsFound_thenReturnApplicationsPage() {

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withValidValues()
                .withoutOrders()
                .build();
        EmergencyEvacuationApplicationFilter mockFilter = new EmergencyEvacuationApplicationFilterBuilder()
                .withStatus(Set.of(EmergencyEvacuationApplicationStatus.PENDING))
                .build();

        // When
        List<EmergencyEvacuationApplicationEntity> mockEntities = List.of(
                new EmergencyEvacuationApplicationEntityBuilder()
                        .withValidValues()
                        .withoutApplicant()
                        .withStatus(EmergencyEvacuationApplicationStatus.PENDING)
                        .build()
        );
        Page<EmergencyEvacuationApplicationEntity> mockApplicationEntitiesPage = new PageImpl<>(mockEntities);
        Mockito.when(emergencyEvacuationApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockApplicationEntitiesPage);

        List<EmergencyEvacuationApplication> mockApplications = emergencyEvacuationApplicationEntityToDomainMapper
                .map(mockEntities);
        AysPage<EmergencyEvacuationApplication> mockApplicationsPage = AysPageBuilder
                .from(mockApplications, mockAysPageable, mockFilter);

        // Then
        AysPage<EmergencyEvacuationApplication> applicationsPage = emergencyEvacuationApplicationAdapter
                .findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockApplicationsPage, applicationsPage);

        // Verify
        Mockito.verify(emergencyEvacuationApplicationRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void givenEmergencyEvacuationApplicationId_whenGettingEmergencyEvacuationApplication_thenReturnEmergencyEvacuationApplication() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        EmergencyEvacuationApplicationEntity mockEntity = new EmergencyEvacuationApplicationEntityBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(emergencyEvacuationApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        Optional<EmergencyEvacuationApplication> emergencyEvacuationApplication = emergencyEvacuationApplicationAdapter.findById(mockId);

        // Verify
        Mockito.verify(emergencyEvacuationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        // Assert
        Assertions.assertTrue(emergencyEvacuationApplication.isPresent());
        Assertions.assertEquals(mockId, emergencyEvacuationApplication.get().getId());
    }

    @Test
    void givenValidEmergencyEvacuationApplication_whenApplicationSaved_thenReturnSavedApplication() {
        // Given
        EmergencyEvacuationApplication mockApplication = new EmergencyEvacuationApplicationBuilder()
                .withValidValues()
                .build();

        mockApplication.pending();

        // When
        EmergencyEvacuationApplicationEntity mockApplicationEntity = emergencyEvacuationApplicationToEntityMapper
                .map(mockApplication);

        Mockito
                .when(emergencyEvacuationApplicationRepository.save(Mockito.any(EmergencyEvacuationApplicationEntity.class)))
                .thenReturn(mockApplicationEntity);

        // When
        emergencyEvacuationApplicationAdapter.save(mockApplication);

        // Then
        Mockito.verify(emergencyEvacuationApplicationRepository, times(1))
                .save(Mockito.any(EmergencyEvacuationApplicationEntity.class));
    }

}
