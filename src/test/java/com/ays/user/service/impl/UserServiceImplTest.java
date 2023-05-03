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
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    void shouldGetAllUsers() {
        // Given
        UserListRequest mockUserListRequest = UserListRequestBuilder.VALID.build();

        List<UserEntity> mockUserEntities = Collections.singletonList(new UserEntityBuilder().build());
        Page<UserEntity> mockPageUserEntities = new PageImpl<>(mockUserEntities);

        List<User> mockUsers = USER_ENTITY_TO_USER_MAPPER.map(mockUserEntities);
        AysPage<User> mockAysPageUsers = AysPage.of(mockPageUserEntities, mockUsers);

        // When
        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(mockPageUserEntities);
        Mockito.when(userEntityToUserMapper.map(mockPageUserEntities.getContent()))
                .thenReturn(mockUsers);

        // Then
        AysPage<User> aysPageUsers = userService.getAllUsers(mockUserListRequest);

        AysPageBuilder.assertEquals(mockAysPageUsers, aysPageUsers);
    }

    @Test
    void shouldGetUserById() {
        // Given
        String id = "1";
        UserEntity userEntity = new UserEntity();
        User user = User.builder().build();

        // When
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        Mockito.when(userEntityToUserMapper.map(userEntity)).thenReturn(user);

        User result = userService.getUserById(id);

        // Then
        Assertions.assertEquals(user.getFirstName(), result.getFirstName());
    }

    @Test
    void shouldGetUserByIdWithNonexistentUser() {
        // Given
        String id = "1";

        AysUserNotExistByIdException expectedError =
                new AysUserNotExistByIdException(id);

        // When
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // then
        AysUserNotExistByIdException actual =
                Assertions.assertThrows(AysUserNotExistByIdException.class, () -> userService.getUserById(id));
        Assertions.assertEquals(expectedError.getMessage(), actual.getMessage());
    }

    @Test
    void shouldDeleteUser() {
        // given
        String id = "1";
        UserEntity userEntity = new UserEntity();
        userEntity.setStatus(UserStatus.ACTIVE);

        // when
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        Mockito.doAnswer(invocation -> {
            userEntity.setStatus(UserStatus.DELETED);
            return null;
        }).when(userRepository).save(userEntity);

        // then
        userService.deleteUser(id);

        Assertions.assertEquals(UserStatus.DELETED, userEntity.getStatus());

    }

    @Test
    void shouldDeleteUserWithNonExistentUser() {
        // Given
        String id = "1";

        AysUserNotExistByIdException expectedError = new AysUserNotExistByIdException(id);

        // When
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // then
        AysUserNotExistByIdException actual =
                Assertions.assertThrows(AysUserNotExistByIdException.class, () -> userService.deleteUser(id));
        Assertions.assertEquals(expectedError.getMessage(), actual.getMessage());
    }

    @Disabled
    @Test
    void updateUser() {
        // Given

        // When

        // Then
    }
}