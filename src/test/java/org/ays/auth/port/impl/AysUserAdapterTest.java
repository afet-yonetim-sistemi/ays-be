package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.auth.model.entity.AysUserEntityBuilder;
import org.ays.auth.model.mapper.AysUserEntityToDomainMapper;
import org.ays.auth.model.mapper.AysUserToEntityMapper;
import org.ays.auth.repository.AysUserRepository;
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
    void givenValidId_whenUserFoundById_thenReturnOptionalEmpty() {

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

}
