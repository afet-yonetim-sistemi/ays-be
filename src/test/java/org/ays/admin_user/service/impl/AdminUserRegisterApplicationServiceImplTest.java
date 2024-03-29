package org.ays.admin_user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.admin_user.model.AdminUserRegisterApplication;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequestBuilder;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.admin_user.model.entity.AdminUserEntityBuilder;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.admin_user.model.enums.AdminUserStatus;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import org.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import org.ays.admin_user.repository.AdminUserRepository;
import org.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdAndStatusException;
import org.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdException;
import org.ays.admin_user.util.exception.AysAdminUserRegisterApplicationSummaryNotExistByIdException;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.institution.repository.InstitutionRepository;
import org.ays.institution.util.exception.AysInstitutionNotExistException;
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

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private InstitutionRepository institutionRepository;

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
    void givenValidAdminUserRegisterApplicationId_whenThereIsNoAdminUserRegisterApplicationWithWaitingStatus_thenThrowAysAdminUserRegisterApplicationSummaryNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationSummaryNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.getRegistrationApplicationSummaryById(mockId)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);

    }

    @Test
    void givenAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplicationCreateResponse() {

        // Given
        AdminUserRegisterApplicationCreateRequest mockRequest = new AdminUserRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .build();
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .withId(mockRequest.getInstitutionId())
                .build();
        AdminUserRegisterApplicationEntity mockEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(mockInstitutionEntity)
                .withReason(mockRequest.getReason())
                .build();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.save(Mockito.any(AdminUserRegisterApplicationEntity.class)))
                .thenReturn(mockEntity);
        Mockito.when(institutionRepository.existsActiveById(mockRequest.getInstitutionId()))
                .thenReturn(true);

        // Then
        adminUserRegisterApplicationService.createRegistrationApplication(mockRequest);

        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserRegisterApplicationEntity.class));
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsActiveById(mockRequest.getInstitutionId());
    }

    @Test
    void givenInvalidInstitutionId_whenCreatingAdminUserRegisterApplication_thenThrowAysInvalidAdminUserRegisterApplicationReasonException() {

        // Given
        AdminUserRegisterApplicationCreateRequest mockRequest = new AdminUserRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withReason(null)
                .build();

        // When
        Mockito.when(institutionRepository.existsActiveById(mockRequest.getInstitutionId()))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                AysInstitutionNotExistException.class,
                () -> adminUserRegisterApplicationService.createRegistrationApplication(mockRequest)
        );
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationApproved_thenReturnNothing() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserStatus.NOT_VERIFIED)
                .build();
        AdminUserRegisterApplicationEntity mockEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserRegisterApplicationStatus.COMPLETED)
                .withAdminUserId(mockAdminUserEntity.getId())
                .withAdminUser(mockAdminUserEntity)
                .build();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));
        Mockito.when(adminUserRegisterApplicationRepository.save(Mockito.any(AdminUserRegisterApplicationEntity.class)))
                .thenReturn(mockEntity);

        // Then
        adminUserRegisterApplicationService.approveRegistrationApplication(mockId);

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserRegisterApplicationEntity.class));
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserEntity.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationNotExistByIdAndStatusException.class,
                () -> adminUserRegisterApplicationService.approveRegistrationApplication(mockId)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(0))
                .save(Mockito.any(AdminUserRegisterApplicationEntity.class));
        Mockito.verify(adminUserRepository, Mockito.times(0))
                .save(Mockito.any(AdminUserEntity.class));

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFoundWithoutCompletedStatus_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationEntity mockEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserRegisterApplicationStatus.WAITING)
                .build();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationNotExistByIdAndStatusException.class,
                () -> adminUserRegisterApplicationService.approveRegistrationApplication(mockId)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(0))
                .save(Mockito.any(AdminUserRegisterApplicationEntity.class));
        Mockito.verify(adminUserRepository, Mockito.times(0))
                .save(Mockito.any(AdminUserEntity.class));

    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenRejectingAdminUserRegisterApplication_thenReturnNothing() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserStatus.NOT_VERIFIED)
                .build();
        AdminUserRegisterApplicationEntity mockEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withAdminUser(mockAdminUserEntity)
                .withAdminUserId(mockAdminUserEntity.getId())
                .withStatus(AdminUserRegisterApplicationStatus.COMPLETED)
                .build();
        AdminUserRegisterApplicationRejectRequest mockRequest = new AdminUserRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        adminUserRegisterApplicationService.rejectRegistrationApplication(mockId, mockRequest);

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserRegisterApplicationEntity.class));
        Mockito.verify(adminUserRepository, Mockito.times(1))
                .save(Mockito.any(AdminUserEntity.class));
    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationRejectRequest mockRequest = new AdminUserRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationNotExistByIdAndStatusException.class,
                () -> adminUserRegisterApplicationService.rejectRegistrationApplication(mockId, mockRequest)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(0))
                .save(Mockito.any(AdminUserRegisterApplicationEntity.class));
        Mockito.verify(adminUserRepository, Mockito.times(0))
                .save(Mockito.any(AdminUserEntity.class));
    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenAdminUserRegisterApplicationIsNotCompleted_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserEntity mockAdminUserEntity = new AdminUserEntityBuilder()
                .withValidFields()
                .withStatus(AdminUserStatus.NOT_VERIFIED)
                .build();
        AdminUserRegisterApplicationEntity mockEntity = new AdminUserRegisterApplicationEntityBuilder()
                .withValidFields()
                .withAdminUser(mockAdminUserEntity)
                .withAdminUserId(mockAdminUserEntity.getId())
                .withStatus(AdminUserRegisterApplicationStatus.WAITING)
                .build();
        AdminUserRegisterApplicationRejectRequest mockRequest = new AdminUserRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(adminUserRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        Assertions.assertThrows(
                AysAdminUserRegisterApplicationNotExistByIdAndStatusException.class,
                () -> adminUserRegisterApplicationService.rejectRegistrationApplication(mockId, mockRequest)
        );

        // Verify
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(1))
                .findById(mockId);
        Mockito.verify(adminUserRegisterApplicationRepository, Mockito.times(0))
                .save(Mockito.any(AdminUserRegisterApplicationEntity.class));
        Mockito.verify(adminUserRepository, Mockito.times(0))
                .save(Mockito.any(AdminUserEntity.class));
    }

}
