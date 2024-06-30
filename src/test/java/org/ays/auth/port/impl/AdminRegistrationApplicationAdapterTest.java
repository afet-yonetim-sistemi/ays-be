package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AdminRegistrationApplicationBuilder;
import org.ays.auth.model.AdminRegistrationApplicationFilter;
import org.ays.auth.model.AdminRegistrationApplicationFilterBuilder;
import org.ays.auth.model.entity.AdminRegistrationApplicationEntity;
import org.ays.auth.model.entity.AdminRegistrationApplicationEntityBuilder;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.auth.model.mapper.AdminRegistrationApplicationEntityToDomainMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToEntityMapper;
import org.ays.auth.repository.AdminRegistrationApplicationRepository;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPageableBuilder;
import org.ays.common.util.AysRandomUtil;
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

class AdminRegistrationApplicationAdapterTest extends AysUnitTest {

    @InjectMocks
    private AdminRegistrationApplicationAdapter adminRegistrationApplicationAdapter;

    @Mock
    private AdminRegistrationApplicationRepository adminRegistrationApplicationRepository;


    private final AdminRegistrationApplicationEntityToDomainMapper adminRegistrationApplicationEntityToDomainMapper = AdminRegistrationApplicationEntityToDomainMapper.initialize();
    private final AdminRegistrationApplicationToEntityMapper adminRegistrationApplicationToEntityMapper = AdminRegistrationApplicationToEntityMapper.initialize();

    @Test
    @SuppressWarnings("unchecked")
    void givenValidAysPageableWithoutFilter_whenApplicationsFound_thenReturnApplicationsPage() {

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withValidValues()
                .withoutOrders()
                .build();
        AdminRegistrationApplicationFilter mockFilter = new AdminRegistrationApplicationFilterBuilder().build();

        // When
        List<AdminRegistrationApplicationEntity> mockApplicationEntities = List.of(
                new AdminRegistrationApplicationEntityBuilder()
                        .withValidValues()
                        .build()
        );
        Page<AdminRegistrationApplicationEntity> mockApplicationEntitiesPage = new PageImpl<>(mockApplicationEntities);
        Mockito.when(adminRegistrationApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockApplicationEntitiesPage);

        List<AdminRegistrationApplication> mockApplications = adminRegistrationApplicationEntityToDomainMapper
                .map(mockApplicationEntities);
        AysPage<AdminRegistrationApplication> mockApplicationsPage = AysPageBuilder
                .from(mockApplications, mockAysPageable, mockFilter);

        // Then
        AysPage<AdminRegistrationApplication> applicationsPage = adminRegistrationApplicationAdapter
                .findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockApplicationsPage, applicationsPage);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
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
        AdminRegistrationApplicationFilter mockFilter = new AdminRegistrationApplicationFilterBuilder()
                .withStatus(Set.of(AdminRegistrationApplicationStatus.WAITING))
                .build();

        // When
        List<AdminRegistrationApplicationEntity> mockApplicationEntities = List.of(
                new AdminRegistrationApplicationEntityBuilder()
                        .withValidValues()
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );
        Page<AdminRegistrationApplicationEntity> mockApplicationEntitiesPage = new PageImpl<>(mockApplicationEntities);
        Mockito.when(adminRegistrationApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockApplicationEntitiesPage);

        List<AdminRegistrationApplication> mockApplications = adminRegistrationApplicationEntityToDomainMapper
                .map(mockApplicationEntities);
        AysPage<AdminRegistrationApplication> mockApplicationsPage = AysPageBuilder
                .from(mockApplications, mockAysPageable, mockFilter);

        // Then
        AysPage<AdminRegistrationApplication> applicationsPage = adminRegistrationApplicationAdapter
                .findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockApplicationsPage, applicationsPage);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }


    @Test
    void givenValidId_whenApplicationFoundById_thenReturnOptionalApplication() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        AdminRegistrationApplicationEntity mockApplicationEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockApplicationEntity));

        // Then
        Optional<AdminRegistrationApplication> application = adminRegistrationApplicationAdapter.findById(mockId);

        Assertions.assertTrue(application.isPresent());
        Assertions.assertEquals(mockId, application.get().getId());

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(mockId);
    }

    @Test
    void givenValidId_whenApplicationNotFoundById_thenReturnOptionalEmpty() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Optional<AdminRegistrationApplication> application = adminRegistrationApplicationAdapter.findById(mockId);

        Assertions.assertFalse(application.isPresent());

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(mockId);
    }


    @Test
    void givenValidApplication_whenApplicationSaved_thenReturnApplicationFromDatabase() {

        // Given
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withoutId()
                .build();

        // When
        AdminRegistrationApplicationEntity mockApplicationEntity = adminRegistrationApplicationToEntityMapper
                .map(mockApplication);
        String mockId = AysRandomUtil.generateUUID();
        mockApplicationEntity.setId(mockId);
        Mockito.when(adminRegistrationApplicationRepository.save(Mockito.any(AdminRegistrationApplicationEntity.class)))
                .thenReturn(mockApplicationEntity);

        mockApplication.setId(mockId);

        // Then
        AdminRegistrationApplication application = adminRegistrationApplicationAdapter
                .save(mockApplication);

        Assertions.assertNotNull(application);
        Assertions.assertEquals(mockApplication.getId(), application.getId());

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));
    }


}