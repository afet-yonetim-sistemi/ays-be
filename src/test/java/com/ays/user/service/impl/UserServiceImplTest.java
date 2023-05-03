package com.ays.user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPaging;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserListRequest;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.repository.UserRepository;
import com.ays.user.util.exception.AysUserNotExistByIdException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class UserServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEntityToUserMapper userEntityToUserMapper;

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
        AysPaging aysPaging = new AysPaging();
        aysPaging.setPage(1L);
        aysPaging.setPageSize(10L);

        UserListRequest listRequest = new UserListRequest();
        listRequest.setPagination(aysPaging);
        Page<UserEntity> userEntities = new PageImpl<>(Arrays.asList(new UserEntity()));
        List<User> users = Arrays.asList(User.builder().build());

        // When
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userEntities);
        when(userEntityToUserMapper.map(userEntities.getContent())).thenReturn(users);


        AysPage<User> result = userService.getAllUsers(listRequest);

        // then
        assertEquals(userEntities.getTotalElements(), result.getTotalElementCount());
        assertEquals(users.get(0).getFirstName(), result.getContent().get(0).getFirstName());
    }

    @Test
    void shouldGetUserById() {
        // Given
        String id = "1";
        UserEntity userEntity = new UserEntity();
        User user = User.builder().build();

        // When
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(userEntityToUserMapper.map(userEntity)).thenReturn(user);

        User result = userService.getUserById(id);

        // Then
        assertEquals(user.getFirstName(), result.getFirstName());
    }

    @Test
    void shouldGetUserByIdWithNonexistentUser() {
        // Given
        String id = "1";

        AysUserNotExistByIdException expectedError =
                new AysUserNotExistByIdException(id);

        // When
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // then
        AysUserNotExistByIdException actual =
                assertThrows(AysUserNotExistByIdException.class, () -> userService.getUserById(id));
        assertEquals(expectedError.getMessage(), actual.getMessage());
    }

    @Test
    void shouldDeleteUser() {
        // given
        String id = "1";
        UserEntity userEntity = new UserEntity();
        userEntity.setStatus(UserStatus.ACTIVE);

        // when
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        doAnswer(invocation -> {
            userEntity.setStatus(UserStatus.DELETED);
            return null;
        }).when(userRepository).save(userEntity);

        // then
        userService.deleteUser(id);

        assertEquals(UserStatus.DELETED, userEntity.getStatus());

    }

    @Test
    void shouldDeleteUserWithNonExistentUser() {
        // Given
        String id = "1";

        AysUserNotExistByIdException expectedError = new AysUserNotExistByIdException(id);

        // When
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // then
        AysUserNotExistByIdException actual =
                assertThrows(AysUserNotExistByIdException.class, () -> userService.deleteUser(id));
        assertEquals(expectedError.getMessage(), actual.getMessage());
    }

    @Disabled
    @Test
    void updateUser() {
        // Given

        // When

        // Then
    }
}