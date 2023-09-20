package com.ays.user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequestBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserSupportStatus;
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
    private UserSelfServiceImpl userSupportStatusService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AysIdentity identity;

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

        // when
        Mockito.when(identity.getUserId()).thenReturn(mockUserEntity.getId());
        Mockito.when(userRepository.findById(mockUserEntity.getId()))
                .thenReturn(Optional.of(mockUserEntity));
        Mockito.when(assignmentRepository.findByUserId(identity.getUserId()))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(mockUserEntity);

        // then
        userSupportStatusService.updateUserSupportStatus(mockUpdateRequest);

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
                () -> userSupportStatusService.updateUserSupportStatus(mockUpdateRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());

    }

    @Test
    void givenUserSupportStatus_whenUserHasAssignment_thenThrowAysUserAlreadyHasAssignmentException() {

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
                () -> userSupportStatusService.updateUserSupportStatus(userSupportStatusUpdateRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserId(Mockito.anyString());

    }

}
