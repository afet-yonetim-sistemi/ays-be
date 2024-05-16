package org.ays.user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.institution.repository.InstitutionRepository;
import org.ays.institution.util.exception.AysInstitutionNotExistException;
import org.ays.user.model.AdminRegistrationApplication;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCreateRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCreateRequestBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationListRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationListRequestBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationRejectRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationRejectRequestBuilder;
import org.ays.user.model.entity.AdminRegisterApplicationEntityBuilder;
import org.ays.user.model.entity.AdminRegistrationApplicationEntity;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserEntityV2Builder;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.user.model.enums.UserStatus;
import org.ays.user.model.mapper.AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper;
import org.ays.user.repository.AdminRegistrationApplicationRepository;
import org.ays.user.repository.UserRepositoryV2;
import org.ays.user.util.exception.AysAdminRegistrationApplicationNotExistByIdException;
import org.ays.user.util.exception.AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException;
import org.ays.user.util.exception.AysAdminRegistrationApplicationSummaryNotExistByIdException;
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

class AdminRegistrationApplicationServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminRegistrationApplicationServiceImpl adminUserRegisterApplicationService;

    @Mock
    private AdminRegistrationApplicationRepository adminRegistrationApplicationRepository;

    @Mock
    private UserRepositoryV2 userRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    private final AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper = AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.initialize();


    @Test
    void givenAdminUserRegisterApplicationListRequest_whenFilterNotGiven_thenReturnAysPageAdminRegisterApplicationsResponse() {

        // Given
        AdminRegistrationApplicationListRequest listRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues()
                .withFilter(null)
                .build();

        List<AdminRegistrationApplicationEntity> mockEntities = List.of(
                new AdminRegisterApplicationEntityBuilder().withValidFields().build()
        );
        Page<AdminRegistrationApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);

        List<AdminRegistrationApplication> mockList = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.map(mockEntities);
        AysPage<AdminRegistrationApplication> mockAysPage = AysPage.of(listRequest.getFilter(), mockPageEntities, mockList);

        // When
        Mockito.when(adminRegistrationApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageEntities);

        // Then
        AysPage<AdminRegistrationApplication> aysPage = adminUserRegisterApplicationService
                .findAll(listRequest);

        AysPageBuilder.assertEquals(mockAysPage, aysPage);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void givenAdminUserRegisterApplicationListRequest_whenAdminUserRegisterApplicationStatusIsAvailable_thenReturnAysPageAdminRegisterApplicationsResponse() {

        // Given
        AdminRegistrationApplicationListRequest listRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues()
                .withStatuses(List.of(AdminRegistrationApplicationStatus.WAITING))
                .build();

        List<AdminRegistrationApplicationEntity> mockEntities = List.of(
                new AdminRegisterApplicationEntityBuilder().withValidFields().build()
        );
        Page<AdminRegistrationApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);

        List<AdminRegistrationApplication> mockList = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.map(mockEntities);
        AysPage<AdminRegistrationApplication> mockAysPage = AysPage.of(listRequest.getFilter(), mockPageEntities, mockList);

        // When
        Mockito.when(adminRegistrationApplicationRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageEntities);

        // Then
        AysPage<AdminRegistrationApplication> aysPage = adminUserRegisterApplicationService
                .findAll(listRequest);

        AysPageBuilder.assertEquals(mockAysPage, aysPage);

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void givenAdminUserRegisterApplicationId_whenGettingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplication() {

        // Given
        AdminRegistrationApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .build();
        String mockId = mockEntity.getId();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        adminUserRegisterApplicationService.findById(mockId);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

    }

    @Test
    void givenAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.findById(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFoundWithWaitingStatus_thenReturnAdminUserRegisterApplication() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .build();
        AdminRegistrationApplication mockAdminRegistrationApplication = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.map(mockEntity);

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
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
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenThereIsNoAdminUserRegisterApplicationWithWaitingStatus_thenThrowAysAdminUserRegisterApplicationSummaryNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationSummaryNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.findAllSummaryById(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplicationCreateResponse() {

        // Given
        AdminRegistrationApplicationCreateRequest mockRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidFields()
                .build();
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .withId(mockRequest.getInstitutionId())
                .build();
        AdminRegistrationApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withInstitution(mockInstitutionEntity)
                .withReason(mockRequest.getReason())
                .build();

        // When
        Mockito.when(adminRegistrationApplicationRepository.save(Mockito.any(AdminRegistrationApplicationEntity.class)))
                .thenReturn(mockEntity);

        Mockito.when(institutionRepository.existsActiveById(mockRequest.getInstitutionId()))
                .thenReturn(true);

        // Then
        adminUserRegisterApplicationService.create(mockRequest);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));

        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsActiveById(Mockito.anyString());
    }

    @Test
    void givenInvalidInstitutionId_whenCreatingAdminUserRegisterApplication_thenThrowAysInvalidAdminUserRegisterApplicationReasonException() {

        // Given
        AdminRegistrationApplicationCreateRequest mockRequest = new AdminRegistrationApplicationCreateRequestBuilder()
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
        AdminRegistrationApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .withUser(mockUserEntity)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        Mockito.when(adminRegistrationApplicationRepository.save(Mockito.any(AdminRegistrationApplicationEntity.class)))
                .thenReturn(mockEntity);

        // Then
        adminUserRegisterApplicationService.approve(mockId);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterApplicationService.approve(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntityV2.class));

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFoundWithoutCompletedStatus_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterApplicationService.approve(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));

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
        AdminRegistrationApplicationEntity mockApplicationEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUser(mockUserEntity)
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .build();
        AdminRegistrationApplicationRejectRequest mockRejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockApplicationEntity));

        // Then
        adminUserRegisterApplicationService.reject(mockId, mockRejectRequest);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntityV2.class));
    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationRejectRequest mockRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterApplicationService.reject(mockId, mockRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));

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
        AdminRegistrationApplicationEntity mockEntity = new AdminRegisterApplicationEntityBuilder()
                .withValidFields()
                .withUser(mockUserEntity)
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .build();
        AdminRegistrationApplicationRejectRequest mockRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException.class,
                () -> adminUserRegisterApplicationService.reject(mockId, mockRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationRepository, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntityV2.class));
    }

}
