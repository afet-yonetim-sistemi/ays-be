package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AdminRegistrationApplicationBuilder;
import org.ays.auth.model.AdminRegistrationApplicationFilter;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.request.AdminRegistrationApplicationCreateRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationCreateRequestBuilder;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequestBuilder;
import org.ays.auth.model.request.AdminRegistrationApplicationRejectRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationRejectRequestBuilder;
import org.ays.auth.port.AdminRegistrationApplicationReadPort;
import org.ays.auth.port.AdminRegistrationApplicationSavePort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.util.exception.AysAdminRegistrationApplicationAlreadyApprovedException;
import org.ays.auth.util.exception.AysAdminRegistrationApplicationAlreadyRejectedException;
import org.ays.auth.util.exception.AysAdminRegistrationApplicationInCompleteException;
import org.ays.auth.util.exception.AysAdminRegistrationApplicationNotExistByIdException;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.institution.port.InstitutionReadPort;
import org.ays.institution.util.exception.AysInstitutionNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class AdminRegistrationApplicationServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AdminRegistrationApplicationServiceImpl adminUserRegisterApplicationService;

    @Mock
    private AdminRegistrationApplicationReadPort adminRegistrationApplicationReadPort;

    @Mock
    private AdminRegistrationApplicationSavePort adminRegistrationApplicationSavePort;

    @Mock
    private AysUserSavePort userSavePort;

    @Mock
    private InstitutionReadPort institutionReadPort;


    @Test
    void givenAdminUserRegisterApplicationListRequestWithoutFilter_whenApplicationsFound_thenReturnApplicationsPage() {

        // Given
        AdminRegistrationApplicationListRequest mockApplicationListRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues()
                .withFilter(null)
                .build();

        // When
        AysPageable aysPageable = mockApplicationListRequest.getPageable();

        List<AdminRegistrationApplication> mockApplications = List.of(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .build()
        );
        AysPage<AdminRegistrationApplication> mockApplicationsPage = AysPageBuilder
                .from(mockApplications, aysPageable);

        Mockito.when(adminRegistrationApplicationReadPort.findAll(Mockito.any(AysPageable.class), Mockito.any()))
                .thenReturn(mockApplicationsPage);

        // Then
        AysPage<AdminRegistrationApplication> applicationsPage = adminUserRegisterApplicationService
                .findAll(mockApplicationListRequest);

        AysPageBuilder.assertEquals(mockApplicationsPage, applicationsPage);

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findAll(Mockito.any(AysPageable.class), Mockito.any());
    }

    @Test
    void givenAdminUserRegisterApplicationListRequest_whenApplicationsFound_thenReturnApplicationsPage() {

        // Given
        AdminRegistrationApplicationListRequest mockApplicationListRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues()
                .withStatuses(Set.of(AdminRegistrationApplicationStatus.WAITING))
                .build();

        // When
        AysPageable aysPageable = mockApplicationListRequest.getPageable();

        List<AdminRegistrationApplication> mockApplications = List.of(
                new AdminRegistrationApplicationBuilder()
                        .withValidValues()
                        .withStatus(AdminRegistrationApplicationStatus.WAITING)
                        .build()
        );
        AysPage<AdminRegistrationApplication> mockApplicationsPage = AysPageBuilder
                .from(mockApplications, aysPageable);

        Mockito.when(adminRegistrationApplicationReadPort.findAll(Mockito.any(AysPageable.class), Mockito.any(AdminRegistrationApplicationFilter.class)))
                .thenReturn(mockApplicationsPage);

        // Then
        AysPage<AdminRegistrationApplication> applicationsPage = adminUserRegisterApplicationService
                .findAll(mockApplicationListRequest);

        AysPageBuilder.assertEquals(mockApplicationsPage, applicationsPage);

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findAll(Mockito.any(AysPageable.class), Mockito.any(AdminRegistrationApplicationFilter.class));
    }


    @Test
    void givenAdminUserRegisterApplicationId_whenGettingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplication() {

        // Given
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .build();
        String mockId = mockApplication.getId();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        // Then
        adminUserRegisterApplicationService.findById(mockId);

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

    }

    @Test
    void givenAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.findById(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFoundWithWaitingStatus_thenReturnAdminUserRegisterApplication() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withId(mockId)
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        // Then
        AdminRegistrationApplication adminRegistrationApplication = adminUserRegisterApplicationService.findSummaryById(mockId);

        Assertions.assertEquals(mockApplication.getId(), adminRegistrationApplication.getId());
        Assertions.assertEquals(mockApplication.getStatus(), adminRegistrationApplication.getStatus());
        Assertions.assertEquals(mockApplication.getReason(), adminRegistrationApplication.getReason());
        Assertions.assertEquals(mockApplication.getRejectReason(), adminRegistrationApplication.getRejectReason());
        Assertions.assertEquals(mockApplication.getUser().getId(), adminRegistrationApplication.getUser().getId());
        Assertions.assertEquals(mockApplication.getInstitution().getId(), adminRegistrationApplication.getInstitution().getId());

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenThereIsNoAdminUserRegisterApplicationWithWaitingStatus_thenThrowAysAdminUserRegisterApplicationSummaryNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.findSummaryById(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplicationCreateResponse() {

        // Given
        AdminRegistrationApplicationCreateRequest mockCreateRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidValues()
                .build();
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId(mockCreateRequest.getInstitutionId())
                .build();
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withInstitution(mockInstitution)
                .withReason(mockCreateRequest.getReason())
                .build();

        // When
        Mockito.when(adminRegistrationApplicationSavePort.save(Mockito.any(AdminRegistrationApplication.class)))
                .thenReturn(mockApplication);

        Mockito.when(institutionReadPort.existsByIdAndIsStatusActive(mockCreateRequest.getInstitutionId()))
                .thenReturn(true);

        // Then
        adminUserRegisterApplicationService.create(mockCreateRequest);

        // Verify
        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplication.class));

        Mockito.verify(institutionReadPort, Mockito.times(1))
                .existsByIdAndIsStatusActive(Mockito.anyString());
    }

    @Test
    void givenInvalidInstitutionId_whenCreatingAdminUserRegisterApplication_thenThrowAysInvalidAdminUserRegisterApplicationReasonException() {

        // Given
        AdminRegistrationApplicationCreateRequest mockRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidValues()
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
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withStatus(AysUserStatus.NOT_VERIFIED)
                .build();
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .withUser(mockUser)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        Mockito.when(adminRegistrationApplicationSavePort.save(Mockito.any(AdminRegistrationApplication.class)))
                .thenReturn(mockApplication);

        // Then
        adminUserRegisterApplicationService.approve(mockId);

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplication.class));

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminRegistrationApplicationNotExistByIdException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.approve(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationWaiting_thenThrowAysAdminRegistrationApplicationInCompleteException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationInCompleteException.class,
                () -> adminUserRegisterApplicationService.approve(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationApproved_thenThrowAysAdminRegistrationApplicationAlreadyApprovedException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withStatus(AdminRegistrationApplicationStatus.APPROVED)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationAlreadyApprovedException.class,
                () -> adminUserRegisterApplicationService.approve(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationRejected_thenThrowAysAdminRegistrationApplicationAlreadyRejectedException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withStatus(AdminRegistrationApplicationStatus.REJECTED)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationAlreadyRejectedException.class,
                () -> adminUserRegisterApplicationService.approve(mockId)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenRejectingAdminUserRegisterApplication_thenReturnNothing() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withStatus(AysUserStatus.NOT_VERIFIED)
                .build();
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withUser(mockUser)
                .withStatus(AdminRegistrationApplicationStatus.COMPLETED)
                .build();
        AdminRegistrationApplicationRejectRequest mockRejectRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        // Then
        adminUserRegisterApplicationService.reject(mockId, mockRejectRequest);

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.times(1))
                .save(Mockito.any(AdminRegistrationApplication.class));

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenAdminUserRegisterApplicationNotFound_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationRejectRequest mockRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationNotExistByIdException.class,
                () -> adminUserRegisterApplicationService.reject(mockId, mockRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenAdminUserRegisterApplicationIdAndAdminUserRegisterApplicationRejectRequest_whenAdminUserRegisterApplicationIsNotCompleted_thenThrowAysAdminUserRegisterApplicationNotExistByIdAndStatusException() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withStatus(AysUserStatus.NOT_VERIFIED)
                .build();
        AdminRegistrationApplication mockApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withUser(mockUser)
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .build();
        AdminRegistrationApplicationRejectRequest mockRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(adminRegistrationApplicationReadPort.findById(mockId))
                .thenReturn(Optional.of(mockApplication));

        // Then
        Assertions.assertThrows(
                AysAdminRegistrationApplicationInCompleteException.class,
                () -> adminUserRegisterApplicationService.reject(mockId, mockRequest)
        );

        // Verify
        Mockito.verify(adminRegistrationApplicationReadPort, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(adminRegistrationApplicationSavePort, Mockito.never())
                .save(Mockito.any(AdminRegistrationApplication.class));

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

}
