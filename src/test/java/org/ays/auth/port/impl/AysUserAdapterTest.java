package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.AysUserFilter;
import org.ays.auth.model.AysUserFilterBuilder;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.auth.model.entity.AysUserEntityBuilder;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.mapper.AysUserEntityToDomainMapper;
import org.ays.auth.model.mapper.AysUserToEntityMapper;
import org.ays.auth.repository.AysUserRepository;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPageableBuilder;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class AysUserAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysUserAdapter userAdapter;

    @Mock
    private AysUserRepository userRepository;


    private final AysUserToEntityMapper userToEntityMapper = AysUserToEntityMapper.initialize();
    private final AysUserEntityToDomainMapper userEntityToDomainMapper = AysUserEntityToDomainMapper.initialize();


    @Test
    @SuppressWarnings("unchecked")
    void givenValidAysPageableWithoutFilter_whenApplicationsFound_thenReturnApplicationsPage() {

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withValidValues()
                .withoutOrders()
                .build();
        AysUserFilter mockFilter = new AysUserFilterBuilder()
                .withValidValues()
                .build();

        // When
        List<AysUserEntity> mockEntities = List.of(
                new AysUserEntityBuilder()
                        .withValidValues()
                        .build()
        );
        Page<AysUserEntity> mockEntitiesPage = new PageImpl<>(mockEntities);
        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockEntitiesPage);

        List<AysUser> mockUsers = userEntityToDomainMapper.map(mockEntities);
        AysPage<AysUser> mockUsersPage = AysPageBuilder.from(mockUsers, mockAysPageable, mockFilter);

        // Then
        AysPage<AysUser> usersPage = userAdapter.findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockUsersPage, usersPage);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenValidAysPageableAndFilter_whenUsersFound_thenReturnUsersPage() {

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withValidValues()
                .withoutOrders()
                .build();
        AysUserFilter mockFilter = new AysUserFilterBuilder()
                .withCity("Mersin")
                .withLastName("Ruiz")
                .withStatuses(Set.of(AysUserStatus.ACTIVE))
                .build();

        // When
        List<AysUserEntity> mockUserEntities = List.of(
                new AysUserEntityBuilder()
                        .withValidValues()
                        .withStatus(AysUserStatus.ACTIVE)
                        .build()
        );
        Page<AysUserEntity> mockUserEntitiesPage = new PageImpl<>(mockUserEntities);
        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockUserEntitiesPage);

        List<AysUser> mockUsers = userEntityToDomainMapper.map(mockUserEntities);
        AysPage<AysUser> mockUsersPage = AysPageBuilder.from(mockUsers, mockAysPageable, mockFilter);

        // Then
        AysPage<AysUser> usersPage = userAdapter.findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockUsersPage, usersPage);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void givenValidId_whenUserFoundById_thenReturnOptionalUser() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        AysUserEntity mockUserEntity = new AysUserEntityBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(userRepository.findById(mockId))
                .thenReturn(Optional.of(mockUserEntity));

        AysUser mockUser = userEntityToDomainMapper.map(mockUserEntity);

        // Then
        Optional<AysUser> user = userAdapter.findById(mockId);

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(mockUser, user.get());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockId);
    }

    @Test
    void givenValidId_whenUserNotFoundById_thenReturnOptionalEmpty() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(userRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Optional<AysUser> user = userAdapter.findById(mockId);

        Assertions.assertFalse(user.isPresent());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(mockId);
    }


    @Test
    void givenValidEmailAddress_whenUserFoundByEmailAddress_thenReturnOptionalUser() {

        // Given
        String mockEmailAddress = "test@afetyonetimsistemi.org";

        // When
        AysUserEntity mockUserEntity = new AysUserEntityBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();
        Mockito.when(userRepository.findByEmailAddress(mockEmailAddress))
                .thenReturn(Optional.of(mockUserEntity));

        AysUser mockUser = userEntityToDomainMapper.map(mockUserEntity);

        // Then
        Optional<AysUser> user = userAdapter.findByEmailAddress(mockEmailAddress);

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(mockUser, user.get());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmailAddress(mockEmailAddress);
    }

    @Test
    void givenValidEmailAddress_whenUserNotFoundByEmailAddress_thenReturnOptionalEmpty() {

        // Given
        String mockEmailAddress = "test@afetyonetimsistemi.org";

        // When
        Mockito.when(userRepository.findByEmailAddress(mockEmailAddress))
                .thenReturn(Optional.empty());

        // Then
        Optional<AysUser> user = userAdapter.findByEmailAddress(mockEmailAddress);

        Assertions.assertFalse(user.isPresent());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmailAddress(mockEmailAddress);
    }

    @Test
    void givenValidPhoneNumber_whenUserFoundByPhoneNumber_thenReturnOptionalUser() {

        // Given
        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withValidValues()
                .build();

        // When
        AysUserEntity mockUserEntity = new AysUserEntityBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumber)
                .build();
        Mockito.when(userRepository.findByCountryCodeAndLineNumber(mockPhoneNumber.getCountryCode(), mockPhoneNumber.getLineNumber()))
                .thenReturn(Optional.of(mockUserEntity));

        AysUser mockUser = userEntityToDomainMapper.map(mockUserEntity);

        // Then
        Optional<AysUser> user = userAdapter.findByPhoneNumber(mockPhoneNumber);

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(mockUser, user.get());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByCountryCodeAndLineNumber(mockPhoneNumber.getCountryCode(), mockPhoneNumber.getLineNumber());
    }

    @Test
    void givenValidPhoneNumber_whenUserNotFoundByPhoneNumber_thenReturnOptionalEmpty() {

        // Given
        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userRepository.findByCountryCodeAndLineNumber(mockPhoneNumber.getCountryCode(), mockPhoneNumber.getLineNumber()))
                .thenReturn(Optional.empty());

        // Then
        Optional<AysUser> user = userAdapter.findByPhoneNumber(mockPhoneNumber);

        Assertions.assertFalse(user.isPresent());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByCountryCodeAndLineNumber(mockPhoneNumber.getCountryCode(), mockPhoneNumber.getLineNumber());
    }


    @Test
    void givenValidEmailAddress_whenUserExistsByEmailAddress_thenReturnTrue() {

        // Given
        String mockEmailAddress = "test@afetyonetimsistemi.org";

        // When
        Mockito.when(userRepository.existsByEmailAddress(mockEmailAddress))
                .thenReturn(true);

        // Then
        boolean isUserExists = userAdapter.existsByEmailAddress(mockEmailAddress);

        Assertions.assertTrue(isUserExists);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .existsByEmailAddress(mockEmailAddress);
    }

    @Test
    void givenValidEmailAddress_whenUserNotExistsByEmailAddress_thenReturnFalse() {

        // Given
        String mockEmailAddress = "test@afetyonetimsistemi.org";

        // When
        Mockito.when(userRepository.existsByEmailAddress(mockEmailAddress))
                .thenReturn(false);

        // Then
        boolean isUserExists = userAdapter.existsByEmailAddress(mockEmailAddress);

        Assertions.assertFalse(isUserExists);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .existsByEmailAddress(mockEmailAddress);
    }


    @Test
    void givenValidPhoneNumber_whenUserExistsByPhoneNumber_thenReturnTrue() {

        // Given
        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userRepository.existsByCountryCodeAndLineNumber(mockPhoneNumber.getCountryCode(), mockPhoneNumber.getLineNumber()))
                .thenReturn(true);

        // Then
        boolean isUserExists = userAdapter.existsByPhoneNumber(mockPhoneNumber);

        Assertions.assertTrue(isUserExists);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(mockPhoneNumber.getCountryCode(), mockPhoneNumber.getLineNumber());
    }

    @Test
    void givenValidPhoneNumber_whenUserNotExistsByPhoneNumber_thenReturnFalse() {

        // Given
        AysPhoneNumber mockPhoneNumber = new AysPhoneNumberBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userRepository.existsByCountryCodeAndLineNumber(mockPhoneNumber.getCountryCode(), mockPhoneNumber.getLineNumber()))
                .thenReturn(false);

        // Then
        boolean isUserExists = userAdapter.existsByPhoneNumber(mockPhoneNumber);

        Assertions.assertFalse(isUserExists);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .existsByCountryCodeAndLineNumber(mockPhoneNumber.getCountryCode(), mockPhoneNumber.getLineNumber());
    }


    @Test
    void givenValidUserWithoutPasswordAndLoginAttempt_whenUserSaved_thenReturnSavedUser() {

        // Given
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(null)
                .build();

        // When
        String mockId = AysRandomUtil.generateUUID();

        AysUserEntity mockUserEntity = userToEntityMapper.map(mockUser);
        mockUserEntity.setId(mockId);
        Mockito.when(userRepository.save(Mockito.any(AysUserEntity.class)))
                .thenReturn(mockUserEntity);

        mockUser.setId(mockId);

        // Then
        AysUser user = userAdapter.save(mockUser);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(mockUser.getId(), user.getId());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(AysUserEntity.class));
    }


    @Test
    void givenValidUserWithPassword_whenUserSaved_thenReturnSavedUser() {

        // Given
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(null)
                .withPassword(new AysUserBuilder.PasswordBuilder().withValidValues().build())
                .build();

        // When
        String mockId = AysRandomUtil.generateUUID();

        AysUserEntity mockUserEntity = userToEntityMapper.map(mockUser);
        mockUserEntity.setId(mockId);
        Mockito.when(userRepository.save(Mockito.any(AysUserEntity.class)))
                .thenReturn(mockUserEntity);

        mockUser.setId(mockId);

        // Then
        AysUser user = userAdapter.save(mockUser);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(mockUser.getId(), user.getId());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(AysUserEntity.class));
    }


    @Test
    void givenValidUserWithLoginAttempt_whenUserSaved_thenReturnSavedUser() {

        // Given
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(null)
                .withLoginAttempt(new AysUserBuilder.LoginAttemptBuilder().withValidValues().build())
                .build();

        // When
        String mockId = AysRandomUtil.generateUUID();

        AysUserEntity mockUserEntity = userToEntityMapper.map(mockUser);
        mockUserEntity.setId(mockId);
        Mockito.when(userRepository.save(Mockito.any(AysUserEntity.class)))
                .thenReturn(mockUserEntity);

        mockUser.setId(mockId);

        // Then
        AysUser user = userAdapter.save(mockUser);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(mockUser.getId(), user.getId());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(AysUserEntity.class));
    }


    @Test
    void givenValidPasswordId_whenUserFound_thenReturnOptionalUser() {

        // Given
        String mockPasswordId = "4424834a-58d9-4496-bb71-fbf6dfa3c843";

        // When
        AysUserEntity.PasswordEntity mockPasswordEntity = new AysUserEntityBuilder.PasswordEntityBuilder()
                .withValidValues()
                .withId(mockPasswordId)
                .build();
        AysUserEntity mockUserEntity = new AysUserEntityBuilder()
                .withValidValues()
                .withPassword(mockPasswordEntity)
                .build();
        Mockito.when(userRepository.findByPasswordId(Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        AysUser mockUser = userEntityToDomainMapper.map(mockUserEntity);

        // Then
        Optional<AysUser> user = userAdapter.findByPasswordId(mockUser.getPassword().getId());

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(mockPasswordId, user.get().getPassword().getId());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());
    }

    @Test
    void givenValidPasswordId_whenUserNotFound_thenReturnOptionalEmpty() {

        // Given
        String mockPasswordId = "4424834a-58d9-4496-bb71-fbf6dfa3c843";

        // When
        AysUserEntity.PasswordEntity mockPasswordEntity = new AysUserEntityBuilder.PasswordEntityBuilder()
                .withValidValues()
                .build();
        AysUserEntity mockUserEntity = new AysUserEntityBuilder()
                .withValidValues()
                .withPassword(mockPasswordEntity)
                .build();
        Mockito.when(userRepository.findByPasswordId(Mockito.anyString()))
                .thenReturn(Optional.of(mockUserEntity));

        AysUser mockUser = userEntityToDomainMapper.map(mockUserEntity);

        // Then
        Optional<AysUser> user = userAdapter.findByPasswordId(mockUser.getPassword().getId());

        Assertions.assertTrue(user.isPresent());
        Assertions.assertNotEquals(mockPasswordId, user.get().getId());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());
    }

}
