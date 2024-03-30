package org.ays.user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.User;
import org.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import org.ays.user.model.dto.request.UserSupportStatusUpdateRequestBuilder;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.model.enums.UserSupportStatus;
import org.ays.user.model.mapper.UserEntityToUserMapper;
import org.ays.user.repository.UserRepository;
import org.ays.user.util.exception.AysUserNotExistByIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class UserSelfServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private UserSelfServiceImpl userSelfService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AysIdentity identity;


    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();


    @Test
    void whenUserFound_thenReturnUser() {
        // Given
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .build();
        String mockUserId = mockUserEntity.getId();

        User mockUser = userEntityToUserMapper.map(mockUserEntity);

        // When
        Mockito.when(identity.getUserId())
                .thenReturn(mockUserId);

        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        User user = userSelfService.getUserSelfInformation();

        Assertions.assertEquals(mockUser.getId(), user.getId());
        Assertions.assertEquals(mockUser.getUsername(), user.getUsername());

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getUserId();

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }

    @Test
    void whenUserIsNotExistForGetting_thenThrowAysUserNotExistByIdException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getUserId())
                .thenReturn(mockUserId);

        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userSelfService.getUserSelfInformation()
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getUserId();

        Mockito.verify(userRepository)
                .findById(Mockito.anyString());
    }


    @Test
    void givenUserSupportStatus_whenUserRole_thenReturnSuccess() {

        // Given
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .build();

        UserSupportStatus userSupportStatus = UserSupportStatus.READY;

        UserSupportStatusUpdateRequest mockUpdateRequest = new UserSupportStatusUpdateRequestBuilder()
                .withSupportStatus(userSupportStatus)
                .build();

        mockUserEntity.updateSupportStatus(userSupportStatus);

        // When
        Mockito.when(identity.getUserId())
                .thenReturn(mockUserEntity.getId());

        Mockito.when(userRepository.findById(mockUserEntity.getId()))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(mockUserEntity);

        // Then
        userSelfService.updateUserSupportStatus(mockUpdateRequest);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void givenUserSupportStatus_whenUserRole_thenThrowAysUserNotExistByIdException() {

        // Given
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .build();

        UserSupportStatus userSupportStatus = UserSupportStatus.READY;

        UserSupportStatusUpdateRequest mockUpdateRequest = new UserSupportStatusUpdateRequestBuilder()
                .withSupportStatus(userSupportStatus)
                .build();

        mockUserEntity.updateSupportStatus(userSupportStatus);

        // When
        Mockito.when(identity.getUserId())
                .thenReturn(mockUserEntity.getId());

        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenThrow(AysUserNotExistByIdException.class);

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userSelfService.updateUserSupportStatus(mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

}
