package com.ays.user.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.auth.model.AysIdentity;
import com.ays.common.util.AysRandomUtil;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserSaveRequest;
import com.ays.user.model.dto.request.UserSaveRequestBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.repository.UserRepository;
import com.ays.user.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

class UserSaveServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private UserSaveServiceImpl userSaveService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AysIdentity identity;

    @Test
    void givenValidUserSaveRequest_whenUserSaved_thenReturnUser() {
        // Given
        UserSaveRequest mockUserSaveRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .build();

        // When
        List<UserEntity> usersFromDatabase = UserEntityBuilder.generateValidUserEntities(10);
        Mockito.when(userRepository.findAll())
                .thenReturn(usersFromDatabase);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("encodedPassword");

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields().build();
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(mockUserEntity);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        // Then
        userSaveService.saveUser(mockUserSaveRequest);
        
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
    }

    @Test
    void givenUserSaveRequestWithExistingPhoneNumber_whenUserSaved_thenReturnUser() {
        // Given
        UserSaveRequest mockUserSaveRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .build();

        // When
        List<UserEntity> usersFromDatabase = UserEntityBuilder.generateValidUserEntities(10);
        usersFromDatabase.get(0).setCountryCode(mockUserSaveRequest.getPhoneNumber().getCountryCode());
        usersFromDatabase.get(0).setLineNumber(mockUserSaveRequest.getPhoneNumber().getLineNumber());
        Mockito.when(userRepository.findAll())
                .thenReturn(usersFromDatabase);

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByPhoneNumberException.class,
                () -> userSaveService.saveUser(mockUserSaveRequest)
        );

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

}