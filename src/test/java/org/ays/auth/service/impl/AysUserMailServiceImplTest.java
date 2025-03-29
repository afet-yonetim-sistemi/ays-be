package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.config.AysApplicationConfigurationParameter;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.common.model.AysMail;
import org.ays.common.service.AysMailService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

class AysUserMailServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysUserMailServiceImpl userMailService;

    @Mock
    private AysApplicationConfigurationParameter applicationConfigurationParameter;

    @Mock
    private AysMailService mailService;


    @Test
    void givenValidUser_whenMailCreated_thenSendPasswordCreateEmail() {
        // Given
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withPassword(new AysUserBuilder.PasswordBuilder().withValidValues().build())
                .build();

        // When
        Mockito.when(applicationConfigurationParameter.getFeUrl())
                .thenReturn("http://localhost:3000");

        Mockito.doNothing()
                .when(mailService)
                .send(Mockito.any(AysMail.class));

        // Then
        userMailService.sendPasswordCreateEmail(mockUser);

        // Verify
        Mockito.verify(applicationConfigurationParameter, Mockito.times(1))
                .getFeUrl();

        Mockito.verifyNoMoreInteractions(applicationConfigurationParameter);

        Mockito.verify(mailService, Mockito.times(1))
                .send(Mockito.any(AysMail.class));
    }

}
