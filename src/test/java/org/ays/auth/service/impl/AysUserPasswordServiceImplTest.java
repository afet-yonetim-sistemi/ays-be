package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.request.AysForgotPasswordRequest;
import org.ays.auth.model.request.AysForgotPasswordRequestBuilder;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.util.exception.AysEmailAddressNotValidException;
import org.ays.common.model.AysMail;
import org.ays.common.service.AysMailService;
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
    private AysMailService mailService;


    @Test
    void givenValidForgotPasswordRequest_whenEmailExist_thenSendPasswordCreateEmail() {
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

        Mockito.doNothing()
                .when(mailService)
                .send(Mockito.any(AysMail.class));

        // Then
        userPasswordService.forgotPassword(mockForgotPasswordRequest);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(mailService, Mockito.times(1))
                .send(Mockito.any(AysMail.class));
    }

    @Test
    void givenValidForgotPasswordRequest_whenEmailDoesExist_thenThrowAysEmailAddressNotValidException() {
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

        Mockito.verify(mailService, Mockito.never())
                .send(Mockito.any(AysMail.class));
    }

}
