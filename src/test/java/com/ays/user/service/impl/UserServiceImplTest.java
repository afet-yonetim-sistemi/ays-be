package com.ays.user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPageBuilder;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserListRequest;
import com.ays.user.model.dto.request.UserListRequestBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.repository.UserRepository;
import com.ays.user.util.exception.AysUserNotExistByIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class UserServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEntityToUserMapper userEntityToUserMapper;


    private static final UserEntityToUserMapper USER_ENTITY_TO_USER_MAPPER = UserEntityToUserMapper.initialize();

    @Disabled
    @Test
    void shouldSaveUser() {
        // Given

        // When

        // Then
    }

    @Test
    void givenUserListRequest_whenGetAllUsers_thenReturnAllUsers() {
        // Given
        UserListRequest mockUserListRequest = UserListRequestBuilder.VALID.build();

        List<UserEntity> mockUserEntities = Collections.singletonList(new UserEntityBuilder().build());
        Page<UserEntity> mockPageUserEntities = new PageImpl<>(mockUserEntities);

        List<User> mockUsers = USER_ENTITY_TO_USER_MAPPER.map(mockUserEntities);
        AysPage<User> mockAysPageUsers = AysPage.of(mockPageUserEntities, mockUsers);

        // When
        Mockito.when(userRepository.findAll(mockUserListRequest.toPageable()))
                .thenReturn(mockPageUserEntities);
        Mockito.when(userEntityToUserMapper.map(mockPageUserEntities.getContent()))
                .thenReturn(mockUsers);

        // Then
        AysPage<User> aysPageUsers = userService.getAllUsers(mockUserListRequest);

        AysPageBuilder.assertEquals(mockAysPageUsers, aysPageUsers);

        Mockito.verify(userRepository, Mockito.times(1)).findAll(mockUserListRequest.toPageable());

    }

    @Test
    void givenUserId_whenGetUser_thenReturnUser() {
        // Given
        String id = UUID.randomUUID().toString();

        UserEntity userEntity = new UserEntityBuilder()
                .withId(id)
                .build();
        User user = USER_ENTITY_TO_USER_MAPPER.map(userEntity);

        // When
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        Mockito.when(userEntityToUserMapper.map(userEntity)).thenReturn(user);

        User result = userService.getUserById(id);

        // Then
        Assertions.assertEquals(user.getFirstName(), result.getFirstName());

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);


    }

    @Test
    void givenInvalidUserId_whenUserIdNotExist_thenReturnNonExistentUser() {
        // Given
        String id = UUID.randomUUID().toString();

        AysUserNotExistByIdException expectedError =
                new AysUserNotExistByIdException(id);

        // When
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // then
        AysUserNotExistByIdException actual =
                Assertions.assertThrows(AysUserNotExistByIdException.class, () -> userService.getUserById(id));
        Assertions.assertEquals(expectedError.getMessage(), actual.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);

    }

    @Test
    void givenInvalidUserId_whenUserFound_thenReturnUserWithStatusDeleted() {
        // given
        String id = UUID.randomUUID().toString();
        UserEntity userEntity = new UserEntityBuilder()
                .withId(id)
                .withStatus(UserStatus.ACTIVE)
                .build();

        // when
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        userEntity.deleteUser();
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);

        // then
        userService.deleteUser(id);

        Assertions.assertEquals(UserStatus.DELETED, userEntity.getStatus());

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
        Mockito.verify(userRepository, Mockito.times(1)).save(userEntity);
    }

    @Test
    void givenInvalidUserId_whenUserDeletedWithId_thenReturnUserWithStatusDeleted() {
        // Given
        String id = UUID.randomUUID().toString();

        AysUserNotExistByIdException expectedError = new AysUserNotExistByIdException(id);

        // When
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // then
        AysUserNotExistByIdException actual =
                Assertions.assertThrows(AysUserNotExistByIdException.class, () -> userService.deleteUser(id));
        Assertions.assertEquals(expectedError.getMessage(), actual.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
    }

    @Disabled
    @Test
    void updateUser() {
        // Given

        // When

        // Then
    }
}