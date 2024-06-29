package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.auth.model.entity.AysUserEntityBuilder;
import org.ays.auth.model.mapper.AysUserEntityToDomainMapper;
import org.ays.auth.model.mapper.AysUserToEntityMapper;
import org.ays.auth.repository.AysUserRepository;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class AysUserAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysUserAdapter userAdapter;

    @Mock
    private AysUserRepository userRepository;


    private final AysUserToEntityMapper userToEntityMapper = AysUserToEntityMapper.initialize();
    private final AysUserEntityToDomainMapper userEntityToDomainMapper = AysUserEntityToDomainMapper.initialize();


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


}
