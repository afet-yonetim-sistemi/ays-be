package org.ays.auth.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.dto.request.AdminRegistrationApplicationCreateRequestBuilder;
import org.ays.auth.model.dto.request.AdminRegistrationApplicationListRequestBuilder;
import org.ays.auth.model.dto.request.AdminRegistrationApplicationRejectRequestBuilder;
import org.ays.auth.model.entity.AdminRegistrationApplicationEntity;
import org.ays.auth.model.entity.AdminRegistrationApplicationEntityBuilder;
import org.ays.auth.model.entity.UserEntityV2;
import org.ays.auth.model.entity.UserEntityV2Builder;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.mapper.AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper;
import org.ays.auth.model.request.AdminRegistrationApplicationCreateRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationRejectRequest;
import org.ays.auth.repository.AdminRegistrationApplicationRepository;
import org.ays.auth.repository.UserRepositoryV2;
import org.ays.auth.util.exception.AysAdminRegistrationApplicationInCompleteException;
import org.ays.auth.util.exception.AysAdminRegistrationApplicationNotExistByIdException;
import org.ays.auth.util.exception.AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException;
import org.ays.auth.util.exception.AysAdminRegistrationApplicationSummaryNotExistByIdException;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.institution.port.InstitutionReadPort;
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

class AdminRegistrationApplicationServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminRegistrationApplicationServiceImpl adminUserRegisterApplicationService;

    @Mock
    private AdminRegistrationApplicationRepository adminRegistrationApplicationRepository;

    @Mock
    private UserRepositoryV2 userRepository;

    @Mock
    private InstitutionReadPort institutionReadPort;


    private final AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper = AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.initialize();


    @Test
    void givenAdminUserRegisterApplicationListRequest_whenFilterNotGiven_thenReturnAysPageAdminRegisterApplicationsResponse() {

        // Given
        AdminRegistrationApplicationListRequest listRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues()
                .withFilter(null)
                .build();

        List<AdminRegistrationApplicationEntity> mockEntities = List.of(
                new AdminRegistrationApplicationEntityBuilder().withValidFields().build()
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
                new AdminRegistrationApplicationEntityBuilder().withValidFields().build()
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
        AdminRegistrationApplicationEntity mockEntity = new AdminRegistrationApplicationEntityBuilder()
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
        AdminRegistrationApplicationEntity mockEntity = new AdminRegistrationApplicationEntityBuilder()
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
        AdminRegistrationApplicationEntity mockEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidFields()
                .withInstitution(mockInstitutionEntity)
                .withReason(mockRequest.getReason())
                .build();

        // When
        Mockito.when(adminRegistrationApplicationRepository.save(Mockito.any(AdminRegistrationApplicationEntity.class)))
                .thenReturn(mockEntity);

        Mockito.when(institutionReadPort.existsByIdAndIsStatusActive(mockRequest.getInstitutionId()))
                .thenReturn(true);

        // Then
        adminUserRegisterApplicationService.create(mockRequest);

        // Verify
        Mockito.verify(adminRegistrationApplicationRepository, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplicationEntity.class));

        Mockito.verify(institutionReadPort, Mockito.times(1))
                .existsByIdAndIsStatusActive(Mockito.anyString());
    }

    @Test
    void givenInvalidInstitutionId_whenCreatingAdminUserRegisterApplication_thenThrowAysInvalidAdminUserRegisterApplicationReasonException() {

        // Given
        AdminRegistrationApplicationCreateRequest mockRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidFields()
                .withReason(null)
                .build();

        // When
        Mockito.when(institutionReadPort.existsByIdAndIsStatusActive(mockRequest.getInstitutionId()))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                AysInstitutionNotExistException.class,
                () -> adminUserRegisterApplicationService.create(mockRequest)
        );

        // Verify
        Mockito.verify(institutionReadPort, Mockito.times(1))
                .existsByIdAndIsStatusActive(Mockito.anyString());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationApproved_thenReturnNothing() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        UserEntityV2 mockUserEntity = new UserEntityV2Builder()
                .withValidFields()
                .withStatus(UserStatus.NOT_VERIFIED)
                .build();
        AdminRegistrationApplicationEntity mockEntity = new AdminRegistrationApplicationEntityBuilder()
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
        AdminRegistrationApplicationEntity mockEntity = new AdminRegistrationApplicationEntityBuilder()
                .withValidFields()
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationRepository.findById(mockId))
                .thenReturn(Optional.of(mockEntity));

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationInCompleteException.class,
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
        AdminRegistrationApplicationEntity mockApplicationEntity = new AdminRegistrationApplicationEntityBuilder()
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
        AdminRegistrationApplicationEntity mockEntity = new AdminRegistrationApplicationEntityBuilder()
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
                AysAdminRegistrationApplicationInCompleteException.class,
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
