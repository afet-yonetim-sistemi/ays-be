package org.ays.auth.service.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.mapper.AysUserCreateRequestToDomainMapper;
import org.ays.auth.model.request.AysUserCreateRequest;
import org.ays.auth.model.request.AysUserCreateRequestBuilder;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysUserMailService;
import org.ays.auth.util.exception.AysRolesNotExistException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByEmailAddressException;
import org.ays.auth.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.common.model.AysPhoneNumber;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class AysUserCreateServiceImplTest extends AysUnitTest {

    @InjectMocks
    private AysUserCreateServiceImpl userCreateService;

    @Mock
    private AysUserReadPort userReadPort;

    @Mock
    private AysUserSavePort userSavePort;

    @Mock
    private AysRoleReadPort roleReadPort;

    @Mock
    private AysUserMailService userMailService;

    @Mock
    private AysIdentity identity;


    private final AysUserCreateRequestToDomainMapper userCreateRequestToDomainMapper = AysUserCreateRequestToDomainMapper.initialize();


    @Test
    void givenValidUserCreateRequest_whenUserCreated_thenSendCreatePasswordMail() {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(userReadPort.findByPhoneNumber(Mockito.any(AysPhoneNumber.class)))
                .thenReturn(Optional.empty());

        String mockInstitutionId = "b99c9d41-9bf2-4120-a453-1b648197460a";
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);

        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withId(mockInstitutionId)
                .build();
        List<AysRole> mockRoles = mockCreateRequest.getRoleIds().stream()
                .map(id -> new AysRoleBuilder()
                        .withValidValues()
                        .withId(id)
                        .withInstitution(mockInstitution)
                        .build()
                ).toList();
        Mockito.when(roleReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(mockRoles);

        AysUser mockUser = userCreateRequestToDomainMapper.map(mockCreateRequest);
        mockUser.activate();
        mockUser.setInstitution(mockInstitution);
        mockUser.setRoles(mockRoles);
        Mockito.when(userSavePort.save(Mockito.any(AysUser.class)))
                .thenReturn(mockUser);

        Mockito.doNothing()
                .when(userMailService)
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));

        // Then
        userCreateService.create(mockCreateRequest);

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(identity, Mockito.times(2))
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.times(1))
                .save(Mockito.any(AysUser.class));

        Mockito.verify(userMailService, Mockito.times(1))
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidUserCreateRequest_whenUserEmailAddressAlreadyExist_thenThrowUserAlreadyExistsByEmailAddressException() {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .build();
        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByEmailAddressException.class,
                () -> userCreateService.create(mockCreateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.never())
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(userMailService, Mockito.never())
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidUserCreateRequest_whenUserPhoneNumberAlreadyExist_thenThrowUserAlreadyExistsByPhoneNumberException() {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenReturn(Optional.empty());

        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .build();
        Mockito.when(userReadPort.findByPhoneNumber(Mockito.any(AysPhoneNumber.class)))
                .thenReturn(Optional.of(mockUser));

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByPhoneNumberException.class,
                () -> userCreateService.create(mockCreateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.never())
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(userMailService, Mockito.never())
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));
    }

    @Test
    void givenValidUserCreateRequest_whenRolesNotExist_thenThrowRolesNotExistException() {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.when(userReadPort.findByEmailAddress(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(userReadPort.findByPhoneNumber(Mockito.any(AysPhoneNumber.class)))
                .thenReturn(Optional.empty());

        String mockInstitutionId = "b99c9d41-9bf2-4120-a453-1b648197460a";
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);

        Mockito.when(roleReadPort.findAllByIds(Mockito.anySet()))
                .thenReturn(List.of());

        // Then
        Assertions.assertThrows(
                AysRolesNotExistException.class,
                () -> userCreateService.create(mockCreateRequest)
        );

        // Verify
        Mockito.verify(userReadPort, Mockito.times(1))
                .findByEmailAddress(Mockito.anyString());

        Mockito.verify(userReadPort, Mockito.times(1))
                .findByPhoneNumber(Mockito.any(AysPhoneNumber.class));

        Mockito.verify(identity, Mockito.never())
                .getInstitutionId();

        Mockito.verify(roleReadPort, Mockito.times(1))
                .findAllByIds(Mockito.anySet());

        Mockito.verify(userSavePort, Mockito.never())
                .save(Mockito.any(AysUser.class));

        Mockito.verify(userMailService, Mockito.never())
                .sendPasswordCreateEmail(Mockito.any(AysUser.class));
    }

}
