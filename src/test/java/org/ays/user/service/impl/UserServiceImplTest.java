package org.ays.user.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.User;
import org.ays.user.model.dto.request.UserListRequestBuilder;
import org.ays.user.model.dto.request.UserUpdateRequestBuilder;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.model.enums.UserRole;
import org.ays.user.model.enums.UserStatus;
import org.ays.user.model.mapper.UserEntityToUserMapper;
import org.ays.user.model.request.UserListRequest;
import org.ays.user.model.request.UserUpdateRequest;
import org.ays.user.repository.UserRepository;
import org.ays.user.util.exception.AysUserAlreadyActiveException;
import org.ays.user.util.exception.AysUserAlreadyDeletedException;
import org.ays.user.util.exception.AysUserAlreadyPassiveException;
import org.ays.user.util.exception.AysUserNotExistByIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class UserServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AysIdentity identity;


    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();


    @Test
    void givenUserListRequest_whenUsersFound_thenReturnUsers() {
        // Given
        UserListRequest mockUserListRequest = new UserListRequestBuilder().withValidValues().build();

        List<UserEntity> mockUserEntities = Collections.singletonList(new UserEntityBuilder().build());
        Page<UserEntity> mockPageUserEntities = new PageImpl<>(mockUserEntities);

        List<User> mockUsers = userEntityToUserMapper.map(mockUserEntities);
        AysPage<User> mockAysPageUsers = AysPage.of(mockUserListRequest.getFilter(), mockPageUserEntities, mockUsers);

        String mockInstitutionId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageUserEntities);

        // Then
        AysPage<User> aysPageUsers = userService.getAllUsers(mockUserListRequest);

        AysPageBuilder.assertEquals(mockAysPageUsers, aysPageUsers);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenUserId_whenGetUser_thenReturnUser() {
        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionId).build();
        String mockUserId = mockUserEntity.getId();

        User mockUser = userEntityToUserMapper.map(mockUserEntity);

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(userRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        User user = userService.getUserById(mockUserId);

        Assertions.assertEquals(mockUser.getFirstName(), user.getFirstName());

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockUserId, mockInstitutionId);
    }

    @Test
    void givenInvalidUserId_whenUserIsNotExistForGetting_thenThrowAysUserNotExistByIdException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();
        String mockInstitutionId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(userRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userService.getUserById(mockUserId)
        );

        // Verify
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserActive_thenUpdateUser() {
        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE)
                .build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withStatus(UserStatus.ACTIVE)
                .withInstitutionId(mockInstitutionId)
                .build();

        String mockUserId = mockUserEntity.getId();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);

        Mockito.when(userRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        UserEntity mockUserEntityToBeUpdated = new UserEntityBuilder()
                .withValidFields()
                .withId(mockUserId)
                .withInstitutionId(mockInstitutionId)
                .build();
        mockUserEntityToBeUpdated.update(mockUpdateRequest);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(mockUserEntityToBeUpdated);

        // Then
        userService.updateUser(mockUserId, mockUpdateRequest);

        Assertions.assertEquals(mockUpdateRequest.getRole(), mockUserEntity.getRole());
        Assertions.assertEquals(mockUpdateRequest.getStatus(), mockUserEntity.getStatus());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntity.class));
        Mockito.verify(userRepository, Mockito.times(0))
                .saveAndFlush(Mockito.any(UserEntity.class));
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserIsNotExistForSaving_thenThrowAysUserNotExistByIdException() {
        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        String mockUserId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE)
                .build();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(userRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userService.updateUser(mockUserId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserAlreadyActive_thenThrowAysUserAlreadyActiveException() {
        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        String mockUserId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.ACTIVE)
                .build();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withId(mockUserId)
                .withStatus(UserStatus.ACTIVE)
                .withInstitutionId(mockInstitutionId)
                .build();
        Mockito.when(userRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyActiveException.class,
                () -> userService.updateUser(mockUserId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserAlreadyPassive_thenThrowAysUserAlreadyPassiveException() {
        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        String mockUserId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE)
                .build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withId(mockUserId)
                .withStatus(UserStatus.PASSIVE)
                .withRole(UserRole.VOLUNTEER)
                .withInstitutionId(mockInstitutionId)
                .build();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(userRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyPassiveException.class,
                () -> userService.updateUser(mockUserId, mockUpdateRequest)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidUserId_whenUserIsExistAndNotDeleted_thenDeleteUser() {
        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withId(mockUserId)
                .withStatus(UserStatus.ACTIVE)
                .withInstitutionId(mockInstitutionId)
                .build();
        Mockito.when(userRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        UserEntity mockUserEntityToBeDeleted = new UserEntityBuilder()
                .withValidFields()
                .withId(mockUserId)
                .withInstitutionId(mockInstitutionId)
                .build();
        mockUserEntityToBeDeleted.delete();
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(mockUserEntityToBeDeleted);

        // Then
        userService.deleteUser(mockUserId);

        Assertions.assertEquals(UserStatus.DELETED, mockUserEntity.getStatus());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(UserEntity.class));
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidUserId_whenUserAlreadyDeleted_thenThrowAysUserAlreadyDeletedException() {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();
        String mockInstitutionId = AysRandomUtil.generateUUID();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withId(mockUserId)
                .withStatus(UserStatus.DELETED)
                .withInstitutionId(mockInstitutionId)
                .build();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(userRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyDeletedException.class,
                () -> userService.deleteUser(mockUserId)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenInvalidUserId_whenUserIsNotExistForDeleting_thenThrowAysUserNotExistByIdException() {
        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(userRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserNotExistByIdException.class,
                () -> userService.deleteUser(mockUserId)
        );

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }
}