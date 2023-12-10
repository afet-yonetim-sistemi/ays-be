package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequestBuilder;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import com.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import com.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdAndStatusException;
import com.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdException;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPageBuilder;
import com.ays.common.util.AysRandomUtil;
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

class AdminUserRegisterApplicationServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminUserRegisterApplicationServiceImpl adminUserRegisterApplicationService;

    @Mock
    private AdminUserRegisterApplicationRepository adminUserRegisterApplicationRepository;


    private final AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper = AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.initialize();


    @Test
    void givenAdminUserRegisterApplicationListRequest_whenFilterNotGiven_thenReturnAysPageAdminRegisterApplicationsResponse() {

        // Given
        AdminUserRegisterApplicationListRequest listRequest = new AdminUserRegisterApplicationListRequestBuilder()
                .withValidValues()
                .withFilter(null)
                .build();

        List<AdminUserRegisterApplicationEntity> mockEntities = List.of(
                new AdminUserRegisterApplicationEntityBuilder().withValidFields().build()
        );
        Page<AdminUserRegisterApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);

        List<AdminUserRegisterApplication> mockList = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(mockEntities);
        AysPage<AdminUserRegisterApplication> mockAysPage = AysPage.of(listRequest.getFilter(), mockPageEntities, mockList);

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageEntities);

        // Then
        AysPage<AdminUserRegisterApplication> aysPage = adminUserRegisterApplicationService
                .getRegistrationApplications(listRequest);

        AysPageBuilder.assertEquals(mockAysPage, aysPage);

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void givenAdminUserRegisterApplicationListRequest_whenAdminUserRegisterApplicationStatusIsAvailable_thenReturnAysPageAdminRegisterApplicationsResponse() {

        // Given
        AdminUserRegisterApplicationListRequest listRequest = new AdminUserRegisterApplicationListRequestBuilder()
                .withValidValues()
                .withStatuses(List.of(AdminUserRegisterApplicationStatus.WAITING))
                .build();

        List<AdminUserRegisterApplicationEntity> mockEntities = List.of(
                new AdminUserRegisterApplicationEntityBuilder().withValidFields().build()
        );
        Page<AdminUserRegisterApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);

        List<AdminUserRegisterApplication> mockList = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(mockEntities);
        AysPage<AdminUserRegisterApplication> mockAysPage = AysPage.of(listRequest.getFilter(), mockPageEntities, mockList);

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageEntities);

        // Then
        AysPage<AdminUserRegisterApplication> aysPage = adminUserRegisterApplicationService
                .getRegistrationApplications(listRequest);

        AysPageBuilder.assertEquals(mockAysPage, aysPage);

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void givenAdminUserRegisterApplicationId_whenGettingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplication() {

        // Given
        AdminUserRegisterApplicationEntity mockEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        String mockId = mockEntity.getId();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        adminUserRegisterApplicationService.getRegistrationApplicationById(mockId);

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);

    }

    @Test
    void givenAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.getRegistrationApplicationById(mockId)
        );

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFoundWithWaitingStatus_thenReturnAdminUserRegisterApplication() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationEntity mockEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserRegisterApplicationStatus.WAITING)
                .build();
        AdminUserRegisterApplication mockAdminUserRegisterApplication = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(mockEntity);

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        AdminUserRegisterApplication adminUserRegisterApplication = adminUserRegisterApplicationService.getRegistrationApplicationSummaryById(mockId);

        Assertions.assertEquals(mockAdminUserRegisterApplication.getId(), adminUserRegisterApplication.getId());
        Assertions.assertEquals(mockAdminUserRegisterApplication.getStatus(), adminUserRegisterApplication.getStatus());
        Assertions.assertEquals(mockAdminUserRegisterApplication.getReason(), adminUserRegisterApplication.getReason());
        Assertions.assertEquals(mockAdminUserRegisterApplication.getRejectReason(), adminUserRegisterApplication.getRejectReason());
        Assertions.assertEquals(mockAdminUserRegisterApplication.getAdminUser().getId(), adminUserRegisterApplication.getAdminUser().getId());
        Assertions.assertEquals(mockAdminUserRegisterApplication.getInstitution().getId(), adminUserRegisterApplication.getInstitution().getId());

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenThereIsNoAdminUserRegisterApplicationWithWaitingStatus_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationNotExistByIdAndStatusException.class,
                () -> adminUserRegisterApplicationService.getRegistrationApplicationSummaryById(mockId)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);

    }

}
