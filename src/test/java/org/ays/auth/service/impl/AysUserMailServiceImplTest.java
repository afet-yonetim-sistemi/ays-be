package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.common.model.AysMail;
import org.ays.common.service.AysMailService;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

class AysUserMailServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysUserMailServiceImpl userMailService;

    @Mock
    private AysMailService mailService;


    @Test
    void givenValidUser_whenMailCreated_thenSendPasswordCreateEmail() {
        // Given
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withFeUrl("http://localhost:3000")
                .build();

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withPassword(new AysUserBuilder.PasswordBuilder().withValidValues().build())
                .withInstitution(mockInstitution)
                .build();

        // When
        Mockito.doNothing()
                .when(mailService)
                .send(Mockito.any(AysMail.class));

        // Then
        userMailService.sendPasswordCreateEmail(mockUser);

        // Verify
        Mockito.verify(mailService, Mockito.times(1))
                .send(Mockito.any(AysMail.class));
    }

}
