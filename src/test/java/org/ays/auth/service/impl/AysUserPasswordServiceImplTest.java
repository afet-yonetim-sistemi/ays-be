package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.exception.AysEmailAddressNotValidException;
import org.ays.auth.exception.AysUserNotActiveException;
import org.ays.auth.exception.AysUserPasswordCannotChangedException;
import org.ays.auth.exception.AysUserPasswordDoesNotExistException;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.request.AysForgotPasswordRequestBuilder;
import org.ays.auth.model.request.AysPasswordCreateRequest;
import org.ays.auth.model.request.AysPasswordCreateRequestBuilder;
import org.ays.auth.model.request.AysPasswordForgotRequest;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysUserMailService;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void givenValidForgotPasswordRequest_whenUserExistWithPassword_thenSetPasswordForgotAtAndSendPasswordCreateEmail() {
        // Given
        AysPasswordForgotRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser.Password password = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .build();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockForgotPasswordRequest.getEmailAddress())
                .withPassword(password)
                .build();
        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withValue(password.getValue())
                .withForgotAt(LocalDateTime.now())
                .build();
        AysUser mockSavedUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockUser.getId())
                .withEmailAddress(mockUser.getEmailAddress())
                .withPhoneNumber(mockUser.getPhoneNumber())
                .withPassword(mockPassword)
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
    void givenValidForgotPasswordRequest_whenUserExistWithoutPassword_thenCreateTempPasswordAndSendPasswordCreateEmail() {
        // Given
        AysPasswordForgotRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockForgotPasswordRequest.getEmailAddress())
                .build();
        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withValue(AysRandomUtil.generateText(15))
                .withForgotAt(LocalDateTime.now())
                .build();
        AysUser mockSavedUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockUser.getId())
                .withEmailAddress(mockUser.getEmailAddress())
                .withPhoneNumber(mockUser.getPhoneNumber())
                .withPassword(mockPassword)
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
        AysPasswordForgotRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
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
    void givenValidForgotPasswordRequest_whenUserStatusNotActive_thenThrowUserNotActiveException() {
        // Given
        String mockEmailAddress = "randomEmailAddress@ays.org";

        AysPasswordForgotRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .withStatus(AysUserStatus.PASSIVE)
                .build();

        // When
        Mockito.when(userReadPort.findByEmailAddress(mockForgotPasswordRequest.getEmailAddress()))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserNotActiveException.class,
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
        String mockId = "67134f24-49b4-4abe-946e-550d7cf3abd3";

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
                .withForgotAt(LocalDateTime.now().minusMinutes(5))
                .withCreatedAt(LocalDateTime.now().minusDays(1))
                .withUpdatedAt(LocalDateTime.now().minusHours(3))
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
        String mockId = "6b4e213e-b2e6-468c-acec-6768c636be7e";

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
        String mockId = "c009ddf2-a74a-4ef6-b530-d70f49ea4f0e";

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
                .withValue("608a15a8-5e82-4fd8-ac74-308068393e53")
                .withForgotAt(null)
                .withCreatedAt(LocalDateTime.now().minusDays(1))
                .withUpdatedAt(LocalDateTime.now().minusHours(3))
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
        String mockId = "d6c36815-0e33-4224-b9ce-b4acf431b891";

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
                .withValue("608a15a8-5e82-4fd8-ac74-308068393e53")
                .withForgotAt(LocalDateTime.now().minusHours(3))
                .withCreatedAt(LocalDateTime.now().minusDays(1))
                .withUpdatedAt(LocalDateTime.now().minusHours(3))
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
    void givenValidIdAndCreateRequest_whenPasswordExistAndForgotInTwoHours_thenEncryptPasswordAndSetAndForgotAtToNullAndSaveUser() {
        // Given
        String mockId = "1cf3234f-240d-48c4-b557-a4110f5f0391";
        AysPasswordCreateRequest mockCreateRequest = new AysPasswordCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
                .withForgotAt(LocalDateTime.now().minusMinutes(5))
                .withCreatedAt(LocalDateTime.now().minusDays(1))
                .build();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withPassword(mockPassword)
                .build();
        Mockito.when(userReadPort.findByPasswordId(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        String mockEncodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn(mockEncodedPassword);

        AysUser.Password mockSavedPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withValue(mockEncodedPassword)
                .build();
        AysUser mockSavedUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockUser.getId())
                .withEmailAddress(mockUser.getEmailAddress())
                .withPhoneNumber(mockUser.getPhoneNumber())
                .withPassword(mockSavedPassword)
                .build();
        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(mockSavedUser);

        // Then
        userPasswordService.createPassword(mockId, mockCreateRequest);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndCreateRequest_whenPasswordExistAndForgotAtAndUpdatedAtDoesNotExist_thenEncryptPasswordAndSetAndForgotAtToNullAndSaveUser() {
        // Given
        String mockId = "918481b0-6bcc-4b74-9062-27e5e6708c6c";
        AysPasswordCreateRequest mockCreateRequest = new AysPasswordCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
                .withoutForgotAt()
                .withCreatedAt(LocalDateTime.now().minusHours(1))
                .build();
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withPassword(mockPassword)
                .build();
        Mockito.when(userReadPort.findByPasswordId(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        String mockEncodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn(mockEncodedPassword);

        AysUser.Password mockSavedPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withValue(mockEncodedPassword)
                .build();
        AysUser mockSavedUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockUser.getId())
                .withEmailAddress(mockUser.getEmailAddress())
                .withPhoneNumber(mockUser.getPhoneNumber())
                .withPassword(mockSavedPassword)
                .build();
        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(mockSavedUser);

        // Then
        userPasswordService.createPassword(mockId, mockCreateRequest);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenIdAndCreateRequest_whenPasswordDoesExist_thenThrowUserPasswordDoesNotExistException() {
        // Given
        String mockId = "5075b296-6099-4cd5-84e7-cf744aaf2360";
        AysPasswordCreateRequest mockCreateRequest = new AysPasswordCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userReadPort.findByPasswordId(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysUserPasswordDoesNotExistException.class,
                () -> userPasswordService.createPassword(mockId, mockCreateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndCreateRequest_whenPasswordExistWithUpdatedAtAndForgotAtDoesNotExist_thenThrowUserPasswordCannotChangedException() {
        // Given
        String mockId = "548581fd-0f2b-482e-bd99-ef73a07b3e44";
        AysPasswordCreateRequest mockCreateRequest = new AysPasswordCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
                .withForgotAt(null)
                .withCreatedAt(LocalDateTime.now().minusDays(1))
                .withUpdatedAt(LocalDateTime.now().minusHours(3))
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
                () -> userPasswordService.createPassword(mockId, mockCreateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidIdAndCreateRequest_whenPasswordExistAndForgotInThreeHours_thenThrowUserPasswordCannotChangedException() {
        // Given
        String mockId = "498ef088-92b0-41d8-a39a-8d9bae87ce8d";
        AysPasswordCreateRequest mockCreateRequest = new AysPasswordCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser.Password mockPassword = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withId(mockId)
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
                () -> userPasswordService.createPassword(mockId, mockCreateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPasswordId(Mockito.anyString());

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.anyString());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));
    }

}
