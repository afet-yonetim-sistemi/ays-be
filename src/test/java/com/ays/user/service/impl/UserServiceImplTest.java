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
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.repository.UserRepository;
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
    void givenValidUserId_whenUserFound_ThenDeleteUser() {
        // given
        String mockUserId = AysRandomUtil.generateUUID();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withId(mockUserId)
                .withStatus(UserStatus.ACTIVE)
                .build();

        // when
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(mockUserEntity));

        mockUserEntity.deleteUser();
        Mockito.when(userRepository.save(mockUserEntity)).thenReturn(mockUserEntity);

        // then
        userService.deleteUser(mockUserId);

        Assertions.assertEquals(UserStatus.DELETED, mockUserEntity.getStatus());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
        Mockito.verify(userRepository, Mockito.times(1))
                .save(mockUserEntity);
    }

    @Test
    void givenInvalidUserId_whenUserIsNotExistForDeleting_thenThrowAysUserNotExistByIdException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userService.deleteUser(mockUserId)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }

    @Test
    void givenUserUpdateRequest_whenUserFound_ThenDeleteUser() {
        // given
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder().build();
        String mockUserId = mockUpdateRequest.getId();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withId(mockUserId)
                .withStatus(UserStatus.ACTIVE)
                .build();

        // When
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(mockUserEntity));

        mockUserEntity.updateUser(mockUpdateRequest);
        Mockito.when(userRepository.save(mockUserEntity)).thenReturn(mockUserEntity);

        // then
        userService.updateUser(mockUpdateRequest);

        Assertions.assertEquals(mockUpdateRequest.getFirstName(), mockUserEntity.getFirstName());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
        Mockito.verify(userRepository, Mockito.times(1))
                .save(mockUserEntity);
    }

    @Test
    void givenUserUpdateRequest_whenUserIsNotExistForDeleting_thenThrowAysUserNotExistByIdException() {
        // given
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder().build();
        String mockUserId = mockUpdateRequest.getId();

        // When
        Mockito.when(userRepository.findById(mockUserId))
                .thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userService.updateUser(mockUpdateRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockUserId);
    }
}