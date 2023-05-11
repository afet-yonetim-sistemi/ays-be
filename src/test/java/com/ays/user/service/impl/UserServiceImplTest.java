package com.ays.user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPageBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserListRequest;
import com.ays.user.model.dto.request.UserListRequestBuilder;
import com.ays.user.model.dto.request.UserUpdateRequest;
import com.ays.user.model.dto.request.UserUpdateRequestBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.repository.UserRepository;
import com.ays.user.util.exception.AysUserAlreadyActiveException;
import com.ays.user.util.exception.AysUserAlreadyDeletedException;
import com.ays.user.util.exception.AysUserAlreadyPassiveException;
import com.ays.user.util.exception.AysUserNotExistByIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class UserServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;


    private static final UserEntityToUserMapper USER_ENTITY_TO_USER_MAPPER = UserEntityToUserMapper.initialize();

    @Test
    void givenUserListRequest_whenUsersFound_thenReturnUsers() {
        // Given
        UserListRequest mockUserListRequest = new UserListRequestBuilder().withValidValues().build();

        List<UserEntity> mockUserEntities = Collections.singletonList(new UserEntityBuilder().build());
        Page<UserEntity> mockPageUserEntities = new PageImpl<>(mockUserEntities);

        List<User> mockUsers = USER_ENTITY_TO_USER_MAPPER.map(mockUserEntities);
        AysPage<User> mockAysPageUsers = AysPage.of(mockPageUserEntities, mockUsers);

        // When
        Mockito.when(userRepository.findAll(mockUserListRequest.toPageable()))
                .thenReturn(mockPageUserEntities);

        // Then
        AysPage<User> aysPageUsers = userService.getAllUsers(mockUserListRequest);

        AysPageBuilder.assertEquals(mockAysPageUsers, aysPageUsers);

        Mockito.verify(userRepository, Mockito.times(1))
                .findAll(mockUserListRequest.toPageable());
    }

    @Test
    void givenUserId_whenGetUser_thenReturnUser() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withId(mockUserId)
                .build();
        User mockUser = USER_ENTITY_TO_USER_MAPPER.map(mockUserEntity);

        // When
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        User user = userService.getUserById(mockUserId);

        Assertions.assertEquals(mockUser.getFirstName(), user.getFirstName());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }

    @Test
    void givenInvalidUserId_whenUserIsNotExistForGetting_thenThrowAysUserNotExistByIdException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userService.getUserById(mockUserId)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }


    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserActive_thenUpdateUser() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE)
                .build();

        // When
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withId(mockUserId)
                .withStatus(UserStatus.ACTIVE)
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        UserEntity mockUserEntityToBeUpdated = new UserEntityBuilder()
                .withValidFields()
                .withId(mockUserId)
                .build();
        mockUserEntityToBeUpdated.updateUser(mockUpdateRequest);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(mockUserEntityToBeUpdated);

        // Then
        userService.updateUser(mockUserId, mockUpdateRequest);

        Assertions.assertEquals(mockUpdateRequest.getRole(), mockUserEntity.getRole());
        Assertions.assertEquals(mockUpdateRequest.getStatus(), mockUserEntity.getStatus());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserIsNotExistForSaving_thenThrowAysUserNotExistByIdException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE)
                .build();

        // When
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userService.updateUser(mockUserId, mockUpdateRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }

    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserAlreadyActive_thenThrowAysUserAlreadyActiveException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.ACTIVE)
                .build();


        // When
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withId(mockUserId)
                .withStatus(UserStatus.ACTIVE)
                .build();
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyActiveException.class,
                () -> userService.updateUser(mockUserId, mockUpdateRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }

    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserAlreadyPassive_thenThrowAysUserAlreadyPassiveException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE)
                .build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withId(mockUserId)
                .withStatus(UserStatus.PASSIVE)
                .withRole(UserRole.VOLUNTEER)
                .build();

        // When
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyPassiveException.class,
                () -> userService.updateUser(mockUserId, mockUpdateRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }

    @Test
    void givenValidUserId_whenUserIsExistAndNotDeleted_thenDeleteUser() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withId(mockUserId)
                .withStatus(UserStatus.ACTIVE)
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        UserEntity mockUserEntityToBeDeleted = new UserEntityBuilder()
                .withValidFields()
                .withId(mockUserId)
                .build();
        mockUserEntityToBeDeleted.deleteUser();
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(mockUserEntityToBeDeleted);

        // Then
        userService.deleteUser(mockUserId);

        Assertions.assertEquals(UserStatus.DELETED, mockUserEntity.getStatus());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void givenValidUserId_whenUserAlreadyDeleted_thenThrowAysUserAlreadyDeletedException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withId(mockUserId)
                .withStatus(UserStatus.DELETED)
                .build();

        // When
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyDeletedException.class,
                () -> userService.deleteUser(mockUserId)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }

    @Test
    void givenInvalidUserId_whenUserIsNotExistForDeleting_thenThrowAysUserNotExistByIdException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userService.deleteUser(mockUserId)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }
}