package org.ays.auth.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.User;
import org.ays.auth.model.dto.request.UserSaveRequestBuilder;
import org.ays.auth.model.entity.UserEntity;
import org.ays.auth.model.entity.UserEntityBuilder;
import org.ays.auth.model.request.UserSaveRequest;
import org.ays.auth.repository.UserRepository;
import org.ays.auth.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.common.util.AysRandomUtil;
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
        List<UserEntity> usersFromDatabase = List.of(new UserEntityBuilder().withValidFields().build());
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
        User user = userSaveService.saveUser(mockUserSaveRequest);

        Assertions.assertNotNull(user.getUsername());
        Assertions.assertEquals(6, user.getUsername().length());
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertEquals(6, user.getPassword().length());

        // Verify
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
        List<UserEntity> usersFromDatabase = List.of(new UserEntityBuilder().withValidFields().build());
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