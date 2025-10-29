package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.common.model.AysMail;
import org.ays.common.service.AysMailService;
import org.ays.institution.model.Institution;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

class AysUserMailServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysUserMailServiceImpl userMailService;

    @Mock
    private Institution mockInstitution;

    @Mock
    private AysMailService mockMailService;


    @Test
    void givenValidUser_whenMailCreated_thenSendPasswordCreateEmail() {
        // Given
        AysUser user = new AysUserBuilder()
                .withValidValues()
                .withPassword(new AysUserBuilder.PasswordBuilder().withValidValues().build())
                .withInstitution(mockInstitution)
                .build();

        // When
        Mockito.when(mockInstitution.getFeUrl())
                .thenReturn("http://localhost:3000");

        Mockito.doNothing()
                .when(mockMailService)
                .send(Mockito.any(AysMail.class));

        // Then
        userMailService.sendPasswordCreateEmail(user);

        // Verify
        Mockito.verify(mockInstitution, Mockito.times(1))
                .getFeUrl();

        Mockito.verifyNoMoreInteractions(user.getInstitution());

        Mockito.verify(mockMailService, Mockito.times(1))
                .send(Mockito.any(AysMail.class));
    }

}
