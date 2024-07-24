package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.request.AysForgotPasswordRequest;
import org.ays.auth.model.request.AysForgotPasswordRequestBuilder;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysUserMailService;
import org.ays.auth.util.exception.AysEmailAddressNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class AysUserPasswordServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysUserPasswordServiceImpl userPasswordService;

    @Mock
    private AysUserReadPort userReadPort;

    @Mock
    private AysUserSavePort userSavePort;

    @Mock
    private AysUserMailService userMailService;


    @Test
    void givenValidForgotPasswordRequest_whenUserExistWithPassword_thenSendPasswordCreateEmail() {
        // Given
        AysForgotPasswordRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockForgotPasswordRequest.getEmailAddress())
                .withPassword(new AysUserBuilder.PasswordBuilder().withValidValues().build())
                .build();
        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        Mockito.doNothing()
                .when(userMailService)
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));

        // Then
        userPasswordService.forgotPassword(mockForgotPasswordRequest);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(userMailService, Mockito.times(1))
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidForgotPasswordRequest_whenUserExistWithoutPassword_thenCreateTempPasswordAndSendPasswordCreateEmail() {
        // Given
        AysForgotPasswordRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockForgotPasswordRequest.getEmailAddress())
                .build();
        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        AysUser mockSavedUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockUser.getId())
                .withEmailAddress(mockUser.getEmailAddress())
                .withPhoneNumber(mockUser.getPhoneNumber())
                .withPassword(new AysUserBuilder.PasswordBuilder().withValidValues().build())
                .build();
        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(mockSavedUser);

        Mockito.doNothing()
                .when(userMailService)
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));

        // Then
        userPasswordService.forgotPassword(mockForgotPasswordRequest);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));

        Mockito.verify(userMailService, Mockito.times(1))
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidForgotPasswordRequest_whenEmailDoesNotExist_thenThrowAysEmailAddressNotValidException() {
        // Given
        AysForgotPasswordRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenThrow(new AysEmailAddressNotValidException(mockForgotPasswordRequest.getEmailAddress()));

        // Then
        Assertions.assertThrows(
                AysEmailAddressNotValidException.class,
                () -> userPasswordService.forgotPassword(mockForgotPasswordRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(userMailService, Mockito.never())
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));
    }

}
