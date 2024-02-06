package com.ays.user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.common.util.AysRandomUtil;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequestBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.repository.UserRepository;
import com.ays.user.util.exception.AysUserCannotUpdateSupportStatusException;
import com.ays.user.util.exception.AysUserNotExistByIdException;
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
    private AssignmentRepository assignmentRepository;

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
        Mockito.when(identity.getUserId()).thenReturn(mockUserEntity.getId());
        Mockito.when(userRepository.findById(mockUserEntity.getId()))
                .thenReturn(Optional.of(mockUserEntity));
        Mockito.when(assignmentRepository.findByUserId(identity.getUserId()))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(mockUserEntity);

        // Then
        userSelfService.updateUserSupportStatus(mockUpdateRequest);

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserId(Mockito.anyString());
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
        Mockito.when(identity.getUserId()).thenReturn(mockUserEntity.getId());
        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenThrow(AysUserNotExistByIdException.class);

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userSelfService.updateUserSupportStatus(mockUpdateRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }

    @Test
    void givenUserSupportStatus_whenUserHasAssignment_thenThrowAysUserCannotUpdateSupportStatusException() {

        // Given
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .build();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withUserId(mockUserEntity.getId())
                .withUser(mockUserEntity)
                .build();

        UserSupportStatus userSupportStatus = UserSupportStatus.READY;

        UserSupportStatusUpdateRequest userSupportStatusUpdateRequest = new UserSupportStatusUpdateRequestBuilder()
                .withSupportStatus(userSupportStatus)
                .build();

        mockUserEntity.updateSupportStatus(userSupportStatus);

        // When
        Mockito.when(identity.getUserId()).thenReturn(mockUserEntity.getId());
        Mockito.when(userRepository.findById(mockUserEntity.getId()))
                .thenReturn(Optional.of(mockUserEntity));
        Mockito.when(assignmentRepository.findByUserId(identity.getUserId()))
                .thenReturn(Optional.of(mockAssignmentEntity));

        // Then
        Assertions.assertThrows(
                AysUserCannotUpdateSupportStatusException.class,
                () -> userSelfService.updateUserSupportStatus(userSupportStatusUpdateRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserId(Mockito.anyString());
    }

}
