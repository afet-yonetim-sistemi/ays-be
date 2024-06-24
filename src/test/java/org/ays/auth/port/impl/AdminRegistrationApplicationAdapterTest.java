package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AdminRegistrationApplicationFilter;
import org.ays.auth.model.AdminRegistrationApplicationFilterBuilder;
import org.ays.auth.model.entity.AdminRegistrationApplicationEntity;
import org.ays.auth.model.entity.AdminRegistrationApplicationEntityBuilder;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.auth.model.mapper.AdminRegistrationApplicationEntityToDomainMapper;
import org.ays.auth.repository.AdminRegistrationApplicationRepository;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPageableBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

class AdminRegistrationApplicationAdapterTest extends AysUnitTest {

    @InjectMocks
    private AdminRegistrationApplicationAdapter adminRegistrationApplicationAdapter;

    @Mock
    private AdminRegistrationApplicationRepository adminRegistrationApplicationRepository;


    private final AdminRegistrationApplicationEntityToDomainMapper adminRegistrationApplicationEntityToDomainMapper = AdminRegistrationApplicationEntityToDomainMapper.initialize();

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

}