package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.common.model.AysMail;
import org.ays.common.service.AysMailService;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.model.AysParameterBuilder;
import org.ays.parameter.port.AysParameterReadPort;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class AysUserMailServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysUserMailServiceImpl userMailService;

    @Mock
    private AysParameterReadPort parameterReadPort;

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
        AysParameter mockParameter = new AysParameterBuilder()
                .withName("FE_URL")
                .withDefinition("http://localhost:3000")
                .build();
        Mockito.when(parameterReadPort.findByName(Mockito.anyString()))
                .thenReturn(Optional.of(mockParameter));

        Mockito.doNothing()
                .when(mailService)
                .send(Mockito.any(AysMail.class));

        // Then
        userMailService.sendPasswordCreateEmail(mockUser);

        // Verify
        Mockito.verify(parameterReadPort, Mockito.times(1))
                .findByName(Mockito.anyString());

        Mockito.verify(mailService, Mockito.times(1))
                .send(Mockito.any(AysMail.class));
    }

}
