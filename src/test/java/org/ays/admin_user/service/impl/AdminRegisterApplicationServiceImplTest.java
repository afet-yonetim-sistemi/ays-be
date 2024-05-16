package org.ays.admin_user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.admin_user.model.AdminRegistrationApplication;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCreateRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCreateRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationListRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationListRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationRejectRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationRejectRequestBuilder;
import org.ays.admin_user.model.entity.AdminRegisterApplicationEntity;
import org.ays.admin_user.model.entity.AdminRegisterApplicationEntityBuilder;
import org.ays.admin_user.model.enums.AdminRegisterApplicationStatus;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationEntityToAdminRegisterApplicationMapper;
import org.ays.admin_user.repository.AdminRegisterApplicationRepository;
import org.ays.admin_user.util.exception.AysAdminRegisterApplicationNotExistByIdException;
import org.ays.admin_user.util.exception.AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException;
import org.ays.admin_user.util.exception.AysAdminRegisterApplicationSummaryNotExistByIdException;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.institution.repository.InstitutionRepository;
import org.ays.institution.util.exception.AysInstitutionNotExistException;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserEntityV2Builder;
import org.ays.user.model.enums.UserStatus;
import org.ays.user.repository.UserRepositoryV2;
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

class AdminRegisterApplicationServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminRegisterApplicationServiceImpl adminUserRegisterApplicationService;

    @Mock
    private AdminRegisterApplicationRepository adminRegisterApplicationRepository;

    @Mock
    private UserRepositoryV2 userRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    private final AdminRegisterApplicationEntityToAdminRegisterApplicationMapper adminRegisterApplicationEntityToAdminRegisterApplicationMapper = AdminRegisterApplicationEntityToAdminRegisterApplicationMapper.initialize();


    @Test
    void givenAdminUserRegisterApplicationListRequest_whenFilterNotGiven_thenReturnAysPageAdminRegisterApplicationsResponse() {

        // Given
        AdminRegisterApplicationListRequest listRequest = new AdminRegisterApplicationListRequestBuilder()
                .withValidValues()
                .withFilter(null)
                .build();

        List<AdminRegisterApplicationEntity> mockEntities = List.of(
                new AdminRegisterApplicationEntityBuilder().withValidFields().build()
        );
        Page<AdminRegisterApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);

        List<AdminRegistrationApplication> mockList = adminRegisterApplicationEntityToAdminRegisterApplicationMapper.map(mockEntities);
        AysPage<AdminRegistrationApplication> mockAysPage = AysPage.of(listRequest.getFilter(), mockPageEntities, mockList);

        // When
        Mockito.when(adminRegisterApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageEntities);

        // Then
        AysPage<AdminRegistrationApplication> aysPage = adminUserRegisterApplicationService
                .findAll(listRequest);

        AysPageBuilder.assertEquals(mockAysPage, aysPage);

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void givenAdminUserRegisterApplicationListRequest_whenAdminUserRegisterApplicationStatusIsAvailable_thenReturnAysPageAdminRegisterApplicationsResponse() {

        // Given
        AdminRegisterApplicationListRequest listRequest = new AdminRegisterApplicationListRequestBuilder()
                .withValidValues()
                .withStatuses(List.of(AdminRegisterApplicationStatus.WAITING))
                .build();

        List<AdminRegisterApplicationEntity> mockEntities = List.of(
                new AdminRegisterApplicationEntityBuilder().withValidFields().build()
        );
        Page<AdminRegisterApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);

        List<AdminRegistrationApplication> mockList = adminRegisterApplicationEntityToAdminRegisterApplicationMapper.map(mockEntities);
        AysPage<AdminRegistrationApplication> mockAysPage = AysPage.of(listRequest.getFilter(), mockPageEntities, mockList);

        // When
        Mockito.when(adminRegisterApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageEntities);

        // Then
        AysPage<AdminRegistrationApplication> aysPage = adminUserRegisterApplicationService
                .findAll(listRequest);

        AysPageBuilder.assertEquals(mockAysPage, aysPage);

        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void givenAdminUserRegisterApplicationId_whenGettingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplication() {

        // Given
        AdminRegisterApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        String mockId = mockEntity.getId();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        adminUserRegisterApplicationService.findById(mockId);

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

    }

    @Test
    void givenAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegisterApplicationNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.findById(mockId)
        );

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFoundWithWaitingStatus_thenReturnAdminUserRegisterApplication() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegisterApplicationStatus.WAITING)
                .build();
        AdminRegistrationApplication mockAdminRegistrationApplication = adminRegisterApplicationEntityToAdminRegisterApplicationMapper.map(mockEntity);

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        AdminRegistrationApplication adminRegistrationApplication = adminUserRegisterApplicationService.findAllSummaryById(mockId);

        Assertions.assertEquals(mockAdminRegistrationApplication.getId(), adminRegistrationApplication.getId());
        Assertions.assertEquals(mockAdminRegistrationApplication.getStatus(), adminRegistrationApplication.getStatus());
        Assertions.assertEquals(mockAdminRegistrationApplication.getReason(), adminRegistrationApplication.getReason());
        Assertions.assertEquals(mockAdminRegistrationApplication.getRejectReason(), adminRegistrationApplication.getRejectReason());
        Assertions.assertEquals(mockAdminRegistrationApplication.getUser().getId(), adminRegistrationApplication.getUser().getId());
        Assertions.assertEquals(mockAdminRegistrationApplication.getInstitution().getId(), adminRegistrationApplication.getInstitution().getId());

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenThereIsNoAdminUserRegisterApplicationWithWaitingStatus_thenThrowAysAdminUserRegisterApplicationSummaryNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegisterApplicationSummaryNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.findAllSummaryById(mockId)
        );

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplicationCreateResponse() {

        // Given
        AdminRegisterApplicationCreateRequest mockRequest = new AdminRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .build();
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .withId(mockRequest.getInstitutionId())
                .build();
        AdminRegisterApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withInstitution(mockInstitutionEntity)
                .withReason(mockRequest.getReason())
                .build();

        // When
        Mockito.when(adminRegisterApplicationRepository.save(Mockito.any(AdminRegisterApplicationEntity.class)))
                .thenReturn(mockEntity);

        Mockito.when(institutionRepository.existsActiveById(mockRequest.getInstitutionId()))
                .thenReturn(true);

        // Then
        adminUserRegisterApplicationService.create(mockRequest);

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegisterApplicationEntity.class));

        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsActiveById(Mockito.anyString());
    }

    @Test
    void givenInvalidInstitutionId_whenCreatingAdminUserRegisterApplication_thenThrowAysInvalidAdminUserRegisterApplicationReasonException() {

        // Given
        AdminRegisterApplicationCreateRequest mockRequest = new AdminRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withReason(null)
                .build();

        // When
        Mockito.when(institutionRepository.existsActiveById(mockRequest.getInstitutionId()))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                AysInstitutionNotExistException.class,
                () -> adminUserRegisterApplicationService.create(mockRequest)
        );

        // Verify
        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsActiveById(Mockito.anyString());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationApproved_thenReturnNothing() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .withStatus(UserStatus.NOT_VERIFIED)
                .build();
        AdminRegisterApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegisterApplicationStatus.COMPLETED)
                .withUser(mockUserEntity)
                .build();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        Mockito.when(adminRegisterApplicationRepository.save(Mockito.any(AdminRegisterApplicationEntity.class)))
                .thenReturn(mockEntity);

        // Then
        adminUserRegisterApplicationService.approve(mockId);

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegisterApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterApplicationService.approve(mockId)
        );

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegisterApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegisterApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntityV2.class));

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFoundWithoutCompletedStatus_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegisterApplicationStatus.WAITING)
                .build();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        Assertions.assertThrows(
                AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterApplicationService.approve(mockId)
        );

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegisterApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegisterApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntityV2.class));

    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenRejectingAdminUserRegisterApplication_thenReturnNothing() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .withStatus(UserStatus.NOT_VERIFIED)
                .build();
        AdminRegisterApplicationEntity mockApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUser(mockUserEntity)
                .withStatus(AdminRegisterApplicationStatus.COMPLETED)
                .build();
        AdminRegisterApplicationRejectRequest mockRejectRequest = new AdminRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockApplicationEntity));

        // Then
        adminUserRegisterApplicationService.reject(mockId, mockRejectRequest);

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegisterApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.class));
    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationRejectRequest mockRequest = new AdminRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterApplicationService.reject(mockId, mockRequest)
        );

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegisterApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegisterApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntityV2.class));
    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenAdminUserRegisterApplicationIsNotCompleted_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .withStatus(UserStatus.NOT_VERIFIED)
                .build();
        AdminRegisterApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUser(mockUserEntity)
                .withStatus(AdminRegisterApplicationStatus.WAITING)
                .build();
        AdminRegisterApplicationRejectRequest mockRequest = new AdminRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(adminRegisterApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        Assertions.assertThrows(
                AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterApplicationService.reject(mockId, mockRequest)
        );

        // Verify
        Mockito.verify(adminRegisterApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegisterApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegisterApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntityV2.class));
    }

}
