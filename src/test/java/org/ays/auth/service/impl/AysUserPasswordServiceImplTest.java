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
import org.ays.auth.util.exception.AysUserPasswordCannotChangedException;
import org.ays.auth.util.exception.AysUserPasswordDoesNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
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
    void givenValidForgotPasswordRequest_whenUserExistWithPassword_thenSetPasswordForgotAtAndSendPasswordCreateEmail() {
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

        AysUser mockSavedUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockUser.getId())
                .withEmailAddress(mockUser.getEmailAddress())
                .withPhoneNumber(mockUser.getPhoneNumber())
                .withPassword(mockUser.getPassword())
                .build();
        mockSavedUser.getPassword().setForgotAt(LocalDateTime.now());
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


    @Test
    void givenValidId_whenPasswordExistAndForgotInTwoHours_thenDoNothing() {
        // Given
        String mockId = "40fb7a46-40bd-46cb-b44f-1f47162133b1";

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
                .withForgotAt(LocalDateTime.now().minusMinutes(5))
                .build();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withPassword(mockPassword)
                .build();
        Mockito.when(userReadPort.findByPasswordId(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Then
        userPasswordService.checkPasswordChangingValidity(mockId);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());
    }

    @Test
    void givenId_whenPasswordDoesExist_thenThrowUserPasswordDoesNotExistException() {
        // Given
        String mockId = "40fb7a46-40bd-46cb-b44f-1f47162133b1";

        // When
        Mockito.when(userReadPort.findByPasswordId(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserPasswordDoesNotExistException.class,
                () -> userPasswordService.checkPasswordChangingValidity(mockId)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());
    }

    @Test
    void givenValidId_whenPasswordExistAndForgotAtDoesNotExist_thenThrowUserPasswordCannotChangedException() {
        // Given
        String mockId = "40fb7a46-40bd-46cb-b44f-1f47162133b1";

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
                .withValue("608a15a8-5e82-4fd8-ac74-308068393e53")
                .withForgotAt(null)
                .build();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withPassword(mockPassword)
                .build();
        Mockito.when(userReadPort.findByPasswordId(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserPasswordCannotChangedException.class,
                () -> userPasswordService.checkPasswordChangingValidity(mockId)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());
    }

    @Test
    void givenValidId_whenPasswordExistAndForgotInThreeHours_thenThrowUserPasswordCannotChangedException() {
        // Given
        String mockId = "40fb7a46-40bd-46cb-b44f-1f47162133b1";

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
                .withValue("608a15a8-5e82-4fd8-ac74-308068393e53")
                .withForgotAt(LocalDateTime.now().minusHours(3))
                .build();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withPassword(mockPassword)
                .build();
        Mockito.when(userReadPort.findByPasswordId(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserPasswordCannotChangedException.class,
                () -> userPasswordService.checkPasswordChangingValidity(mockId)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());
    }

}
