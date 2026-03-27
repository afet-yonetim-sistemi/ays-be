package org.ays.auth.port.adapter;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.auth.model.mapper.AysUserLoginAttemptToEntityMapper;
import org.ays.auth.repository.UserLoginAttemptRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;

class UserLoginAttemptSaveAdapterTest extends AysUnitTest {

    @InjectMocks
    private UserLoginAttemptSaveAdapter userLoginAttemptSaveAdapter;

    @Mock
    private UserLoginAttemptRepository userLoginAttemptRepository;

    private final AysUserLoginAttemptToEntityMapper userLoginAttemptMapper = AysUserLoginAttemptToEntityMapper.initialize();


    @Test
    void givenValidUserWithNewLoginAttempt_whenLoginAttemptSaved_thenDoNothing() {

        // Given
        final String mockUserId = "be2e4921-9afe-4741-81e0-edea52240da5";
        final AysUser.LoginAttempt mockLoginAttempt = new AysUserBuilder.LoginAttemptBuilder()
                .withValidValues()
                .build();

        final AysUserEntity.LoginAttemptEntity mockLoginAttemptEntity = userLoginAttemptMapper.map(mockLoginAttempt);
        mockLoginAttemptEntity.setId("fe269ba3-abdb-4324-8759-53bca27667b6");
        mockLoginAttemptEntity.setCreatedUser("AYS");
        mockLoginAttemptEntity.setCreatedAt(LocalDateTime.now());
        Mockito.when(userLoginAttemptRepository.save(Mockito.any(AysUserEntity.LoginAttemptEntity.class)))
                .thenReturn(mockLoginAttemptEntity);

        // Then
        userLoginAttemptSaveAdapter.save(mockUserId, mockLoginAttempt);

        // Verify
        Mockito.verify(userLoginAttemptRepository, Mockito.times(1))
                .save(Mockito.any(AysUserEntity.LoginAttemptEntity.class));
    }

    @Test
    void givenValidUserWithExistingLoginAttempt_whenLoginAttemptSaved_thenDoNothing() {

        // Given
        final String mockUserId = "e60f522e-7f77-48d5-929f-8d49f7d3b449";
        final AysUser.LoginAttempt mockLoginAttempt = new AysUserBuilder.LoginAttemptBuilder()
                .withValidValues()
                .withId("d4a65325-effc-41b4-9d10-a18823b9cbdf")
                .withCreatedUser("AYS")
                .withCreatedAt(LocalDateTime.now())
                .build();

        final AysUserEntity.LoginAttemptEntity mockLoginAttemptEntity = userLoginAttemptMapper.map(mockLoginAttempt);
        mockLoginAttemptEntity.setUpdatedUser("AYS");
        mockLoginAttemptEntity.setUpdatedAt(LocalDateTime.now());
        Mockito.when(userLoginAttemptRepository.save(Mockito.any(AysUserEntity.LoginAttemptEntity.class)))
                .thenReturn(mockLoginAttemptEntity);

        // Then
        userLoginAttemptSaveAdapter.save(mockUserId, mockLoginAttempt);

        // Verify
        Mockito.verify(userLoginAttemptRepository, Mockito.times(1))
                .save(Mockito.any(AysUserEntity.LoginAttemptEntity.class));
    }

}
