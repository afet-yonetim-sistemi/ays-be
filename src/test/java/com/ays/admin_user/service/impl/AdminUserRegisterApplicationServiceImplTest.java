package com.ays.admin_user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import com.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import com.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdException;
import com.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class AdminUserRegisterApplicationServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AdminUserRegisterApplicationServiceImpl adminUserRegisterApplicationService;

    @Mock
    private AdminUserRegisterApplicationRepository adminUserRegisterApplicationRepository;


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

}
